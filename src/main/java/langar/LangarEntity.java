   package langar;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.List;



public class LangarEntity {
   final public static String NAME = "name";
   final public static String EMAIL = "email";
   final public static String COUNTER = "counter";
   final public static String SEWA = "sewa";
   final private static String SEWAKEY = "Sewa";
   final private static String MAINKEY = "Langar";
   final private static String MAINVALUE = "langar";
   final private static String LASTMODIFIED = "lastmodified";
   public void createLangar() {
     Entity langarEntity = new Entity(MAINKEY, MAINVALUE);
     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(langarEntity);
     Key langarKey = langarEntity.getKey();

     for (int i=0; i<10; i++)
        createSewa(langarKey, "Chapattis", i);
     
     for (int i=0; i<5; i++)
        createSewa(langarKey, "Daal", i);
     for (int i=0; i<1; i++)
        createSewa(langarKey, "DewanSponsor", i);
     for (int i=0; i<2; i++)
        createSewa(langarKey, "SweetDish", i);
     for (int i=0; i<1; i++)
        createSewa(langarKey, "PaperGoods", i);
     for (int i=0; i<1; i++)
        createSewa(langarKey, "Prashad", i);
     for (int i=0; i<2; i++)
        createSewa(langarKey, "Raita", i);
     for (int i=0; i<5; i++)
        createSewa(langarKey, "Rice", i);
     for (int i=0; i<6; i++)
        createSewa(langarKey, "Sabji", i);
     for (int i=0; i<1; i++)
        createSewa(langarKey, "Salad", i);
     
     
  }
  private void createSewa(Key langarKey, String sewa, int counter) {
      Entity sewaEntity = new Entity(SEWAKEY, langarKey);
      sewaEntity.setProperty(SEWA, sewa);
      sewaEntity.setProperty(COUNTER, "" + counter);
      sewaEntity.setProperty(NAME, "");
      sewaEntity.setProperty(EMAIL, "");
      sewaEntity.setProperty(LASTMODIFIED, new Date());

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(sewaEntity);
  }
  
  public Date getLangarClearanceDate() {
      Key langarKey = KeyFactory.createKey(MAINKEY, MAINVALUE);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Entity langarE = null;
      try { langarE = datastore.get(langarKey); }
      catch(Exception ex) { return null; }
      Date lastClearanceDate = (Date) langarE.getProperty("LastResetTime");
      return lastClearanceDate;
  }

  public Date setLangarClearanceDate() {
      Key langarKey = KeyFactory.createKey(MAINKEY, MAINVALUE);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Transaction tx = datastore.beginTransaction();
      Entity langarE = null;
      try { langarE = datastore.get(langarKey); }
      catch(Exception ex) { return null; }
      Date lastClearanceDate = new Date();
      langarE.setProperty("LastResetTime", lastClearanceDate);
      datastore.put(tx, langarE);
      tx.commit();
      return lastClearanceDate;
  }
  public Date getLangarModificationDate() {
      Key langarKey = KeyFactory.createKey(MAINKEY, MAINVALUE);
      Query query = new Query(SEWAKEY).setAncestor(langarKey).addSort(LASTMODIFIED, SortDirection.DESCENDING);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      List<Entity> lastModifiedSewa = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
      Date lastmodified = (Date)lastModifiedSewa.get(0).getProperty(LASTMODIFIED);
      return lastmodified;
  }
  public List<Entity> getLangar() {
     Key langarKey = KeyFactory.createKey(MAINKEY, MAINVALUE);
     //Query query = new Query("LangarEntity", langarKey).setFilter(new FilterPredicate("sewa", FilterOperator.EQUAL, "Sabji"));
     Query query = new Query(SEWAKEY).setAncestor(langarKey);
     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      List<Entity> sewas = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
      return sewas;
  }
  
  public void clearallSewa() throws Exception {
     boolean updateSuccess = false;
     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
     Key langarKey = KeyFactory.createKey(MAINKEY, MAINVALUE);
     Query query = new Query(SEWAKEY).setAncestor(langarKey);
     List<Entity> sewaEntitys = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
     HashMap<String, List<String>> rc = new HashMap<String, List<String>>();
          List<String> assignedList ;
     Transaction tx = datastore.beginTransaction();
          
     for (Entity sewaEntity: sewaEntitys) {
      String existingName = (String)sewaEntity.getProperty(NAME);
      String existingEmail = (String)sewaEntity.getProperty(EMAIL);
      String sewa = (String)sewaEntity.getProperty(SEWA);
      String counter = (String) sewaEntity.getProperty(COUNTER);
      assignedList = rc.get(sewa);
      if (assignedList == null) {
         assignedList = new ArrayList<String>();
         rc.put(sewa, assignedList);
      }
      if (existingName != "") {
         assignedList.add(existingName);
      }
      sewaEntity.setProperty(NAME, "");
      sewaEntity.setProperty(EMAIL, "");
      sewaEntity.setProperty(LASTMODIFIED, new Date());
      updateSuccess = true;
     }     
     datastore.put(tx, sewaEntitys);
     tx.commit();
     if (!updateSuccess) {        
        throw new Exception("ConcurrentModificationException");
     }

     for (String key: rc.keySet()) {
        System.out.print(key + "|");
        for (String sewadar: rc.get(key)) {
           System.out.print(sewadar + ",");
        }
        System.out.println();
     }
     
  }
  
  public ArrayList<String> updateSewa(String sewa, String _counter, String prevName, String newName, String newEmail) throws Exception {
     boolean updateSuccess = false;
     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
     Transaction tx = datastore.beginTransaction();
     Key langarKey = KeyFactory.createKey(MAINKEY, MAINVALUE);
     Query query = new Query(SEWAKEY).setAncestor(langarKey).addFilter(SEWA, FilterOperator.EQUAL, sewa);
     List<Entity> sewaEntitys = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
     ArrayList<String> rc = new ArrayList<String>();
     for (Entity sewaEntity: sewaEntitys) {
        String existingName = (String)sewaEntity.getProperty(NAME);
        String existingEmail = (String)sewaEntity.getProperty(EMAIL);
        String counter = (String) sewaEntity.getProperty(COUNTER);
        if (counter.equals(_counter)) {
           if (existingName == null || existingName.equals("") || existingName.equals(prevName)) {
              sewaEntity.setProperty(NAME, newName);
              sewaEntity.setProperty(EMAIL, newEmail);
              sewaEntity.setProperty(LASTMODIFIED, new Date());
              datastore.put(tx, sewaEntity);
              existingName = newName;
              updateSuccess = true;
              System.out.println("update " + sewa + ", " + _counter + ", " + prevName + ", " + newName + ", " + newEmail);
           } else {
              System.out.println("ConcurrentException? existingName =" + existingName + " prevName=" + prevName + " newName="+ newName);
           }
        }
        if (!existingName.equals("")) {
           rc.add(existingName);
        }
     }     
     tx.commit();
     if (!updateSuccess) {        
        throw new Exception("ConcurrentModificationException");
     }
     
     return rc;
  }
}
