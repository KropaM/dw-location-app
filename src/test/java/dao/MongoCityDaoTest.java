package dao;

import com.cities.api.City;
import com.cities.dao.MongoCityDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fakemongo.Fongo;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import io.dropwizard.jackson.Jackson;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by GKlymenko on 4/6/2016.
 */

public class MongoCityDaoTest {

    private Fongo fongo = new Fongo("mongo server 1");
    private MongoDatabase db;
    private MongoCityDao mongoCityDao;
    private MongoCollection<Document> collection;
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private List<Document> citiesDoc;

    private final Document kyivDoc = new Document()
            .append("name", "Kyiv")
            .append("coordinates", "50.450100,30.523400")
            .append("address", "Kiev, Ukraine, 02000");

    private final Document lvivDoc = new Document()
            .append("name", "Lviv")
            .append("coordinates", "49.839683,24.029717")
            .append("address", "Lviv, Lviv Oblast, Ukraine");

    private City  kyiv;
    private City  lviv;

    private void getValidGiven() throws IOException {
        List<City> givenValidCities = MAPPER.readValue(fixture("fixtures/valid-cities.json"),new TypeReference<List<City>>(){});
        kyiv = givenValidCities.get(0);
        lviv = givenValidCities.get(1);
    }

    @Before
    public void setup() throws Exception {
        db = fongo.getDatabase("testdb");
        collection = db.getCollection("testcities");
        mongoCityDao = new MongoCityDao(db, collection);
        getValidGiven();

        citiesDoc = new ArrayList<>();
        citiesDoc.add(kyivDoc);
        citiesDoc.add(lvivDoc);

        //populate DB
        collection.insertMany(citiesDoc);
    }

    @Test
    public void addCityTest(){
        long countBeforeAdding = collection.count();
        assertThat(mongoCityDao.addCity(kyiv)).isEqualTo(kyiv);
        long countAfterAdding = collection.count();
        assertThat(countBeforeAdding).isLessThan(countAfterAdding);
    }

    @Test
    public void getAllCitiesTest(){
        assertThat(mongoCityDao.getAllCities())
                .isEqualTo(getAllFromInMemory(collection));
    }

    @Test
    public void getCityByName(){
        assertThat(mongoCityDao.getCityByName("Kyiv"))
                .isEqualTo(getCityByNameFromInMemory(kyivDoc));
    }

    @Test
    public void updateCityTest(){
        mongoCityDao.updateCity("Kyiv", lviv);
        assertThat(getCityByNameFromInMemory(kyivDoc)).isNull();
        assertThat(getCityByNameFromInMemory(lvivDoc)).isEqualTo(lviv);
    }

    @Test
    public void removeCityTest(){
        long countBeforeRemove = collection.count();
        assertThat(mongoCityDao.removeCity("Kyiv")).isEqualTo(true);
        long countAfterRemove = collection.count();
        assertThat(countAfterRemove).isLessThan(countBeforeRemove);
    }



    private City getCityByNameFromInMemory(Document doc) {
        FindIterable<Document> iterable = collection.find(doc);
        MongoCursor cursor = iterable.iterator();
        if (cursor.hasNext()){
            Document d = (Document) cursor.next();
            return cityFromDocument(d);
        }
        return null;
    }

    private List<City> getAllFromInMemory(MongoCollection mongoCollection){
        List<City> cities = new ArrayList<City>();
        MongoCursor cursor = mongoCollection.find().iterator();
        while (cursor.hasNext()) {
            Document d = (Document) cursor.next();
            cities.add(cityFromDocument(d));
        }
        return cities;
    }

    private City cityFromDocument(Document document) throws MongoException {
        City city = new City();
        city.setName((String) document.get("name"));
        city.setLongLat((String) document.get("coordinates"));
        city.setFormattedAddress((String) document.get("address"));
        return city;
    }
}
