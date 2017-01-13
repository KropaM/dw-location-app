package com.cities.dao;

import com.cities.api.City;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GKlymenko on 4/6/2016.
 */
public class MongoCityDao implements CityDao {
    private MongoDatabase db;
    private MongoCollection<Document> collection;

    public MongoCityDao(MongoDatabase db, MongoCollection collection) {
        this.db = db;
        this.collection = collection;
    }

    public City addCity(City city) throws MongoException {
        collection.insertOne(
                new Document()
                        .append("name", city.getName())
                        .append("coordinates", city.getLongLat())
                        .append("address", city.getFormattedAddress())
        );
        return city;
    }

    public List<City> getAllCities() throws MongoException {
        List<City> cities = new ArrayList<City>();
        FindIterable<Document> iterable = collection.find();
        MongoCursor cursor = iterable.iterator();
        while (cursor.hasNext()) {
            Document d = (Document) cursor.next();
            cities.add(cityFromDocument(d));
        }
        return cities;
    }

    public City getCityByName(String name) throws MongoException {
        FindIterable<Document> iterable = collection.find(new Document("name", name));
        MongoCursor cursor = iterable.iterator();
        if(cursor.hasNext()) {
            Document d = (Document) cursor.next();
            return cityFromDocument(d);
        }
        return null;
    }

    public boolean updateCity(String name, City city) throws MongoException {
        collection.updateOne(new Document("name", name),
                        new Document("$set", new Document()
                                .append("name", city.getName())
                                .append("coordinates", city.getLongLat())
                                .append("address", city.getFormattedAddress())));
        return true;
    }

    public boolean removeCity(String name) throws MongoException {
        DeleteResult result = collection.deleteMany(new Document("name", name));
        return result.wasAcknowledged();
    }

    private City cityFromDocument(Document document) throws MongoException {
        City city = new City();
        city.setName((String) document.get("name"));
        city.setLongLat((String) document.get("coordinates"));
        city.setFormattedAddress((String) document.get("address"));
        return city;
    }

    public MongoDatabase getDb() {
        return db;
    }

    public void setDb(MongoDatabase db) {
        this.db = db;
    }

    public MongoCollection<Document> getMongoCollection() {
        return collection;
    }

    public void setMongoCollection(MongoCollection<Document> mongoCollection) {
        this.collection = mongoCollection;
    }
}
