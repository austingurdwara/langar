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

import java.io.IOException;
import java.util.Date;
import java.util.List;



public class UserEntity {
   final public static String NAME = "name";
   final public static String EMAIL = "email";
   final public static String SUPERUSER = "superuser";
   final public static String ALLOWUSERMGMT = "AllowUserMgmt";
   final public static String SUPERUSERYES = "yes";
   
   final private static String USERKEY = "User";
   final private static String MAINKEY = "SSGAUser";
   final private static String MAINVALUE = "user";
   
   public void createFirstUser() {
     Entity userEntity = new Entity(MAINKEY, MAINVALUE);
     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(userEntity);
      Key userKey = userEntity.getKey();
      
      userEntity = new Entity(USERKEY, userKey);
      userEntity.setProperty(NAME, "amar");
      userEntity.setProperty(EMAIL, "amarsinghaustin@gmail.com");
      userEntity.setProperty(SUPERUSER, SUPERUSERYES);      
      datastore.put(userEntity);      
  }
  
  public void createOrUpdateUser(String name, String email) {
    this.createOrUpdateUser(name, email,  null, null, true);
  }
  public Entity createOrUpdateUser(String name, String email, String superuser, String allowusermgmt, boolean update) {
     Key userKey = KeyFactory.createKey(MAINKEY, MAINVALUE);
     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
     Transaction tx = datastore.beginTransaction();
     Entity userEntity;
     if (email == null || email.equals("")) {
        userEntity = null;
     } else {
        userEntity = findUser(email);
     }
     if (userEntity == null) {
        userEntity = new Entity(USERKEY, userKey);
        userEntity.setProperty(NAME, name);
        userEntity.setProperty(EMAIL, email);
        if (superuser == null) superuser = "no";
        userEntity.setProperty(SUPERUSER, superuser);      
        if (allowusermgmt == null) allowusermgmt = "no";
        userEntity.setProperty(ALLOWUSERMGMT, allowusermgmt);      
        datastore.put(tx, userEntity);              
     } else if (update) {
        userEntity.setProperty(NAME, name);
        if (superuser != null) {
           userEntity.setProperty(SUPERUSER, superuser);      
        }
        if (allowusermgmt != null) {
           userEntity.setProperty(ALLOWUSERMGMT, allowusermgmt);      
        }
        datastore.put(tx, userEntity);
     }
   
     tx.commit();
     return userEntity;

  }
  
  public void deleteUser(String name, String email) {
     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
     Transaction tx = datastore.beginTransaction();
     try {
        Key userKey = KeyFactory.createKey(MAINKEY, MAINVALUE);
        Query query = new Query(USERKEY).setAncestor(userKey).addFilter(EMAIL, FilterOperator.EQUAL, email).addFilter(NAME, FilterOperator.EQUAL, name);
        Entity userEntity = datastore.prepare(query).asSingleEntity();
        if (userEntity != null)
           datastore.delete(tx, userEntity.getKey());
     } finally {
        if (tx != null)
           tx.commit();
     }
     
  }
  
  /* find user if exists or create one and return Entity */
  public Entity findAndCreateUser(String email) {
     return this.createOrUpdateUser(email, email, null, null, false);
  }

  public Entity findUser(String email) {
     Key userKey = KeyFactory.createKey(MAINKEY, MAINVALUE);
     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
     Query query = new Query(USERKEY).setAncestor(userKey).addFilter(EMAIL, FilterOperator.EQUAL, email);
     Entity userEntity = datastore.prepare(query).asSingleEntity();
     return userEntity;
     
  }
  
  public List<Entity> listUsers() {
     Key userKey = KeyFactory.createKey(MAINKEY, MAINVALUE);
     Query query = new Query(USERKEY).setAncestor(userKey);
     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      List<Entity> users = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
      //System.out.println("UserEntity Query len=" + users.size());
      return users;
  }
  
}
