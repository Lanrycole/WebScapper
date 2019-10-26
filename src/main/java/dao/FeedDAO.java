package dao;

import entity.Feed;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shariar Emami
 * @since Jan 14, 2019
 */
public class FeedDAO extends GenericDAO<Feed> {

    public FeedDAO() {
        super(Feed.class);
    }
    
    public List<Feed> findAll(){
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        return findResults( "Feed.findAll", null);
    }
    
    public Feed findById( int id){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "id and value for it is saved in map
        return findResult( "Feed.findById", map);
    }
    
    public List<Feed> findByName( String name){
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return findResults( "Feed.findByName", map);
    }
    

    public List<Feed> findByType(String type){
          Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        return findResults( "Feed.findByType", map);       
    }
        public List<Feed> findByHostId(int hostId){
          Map<String, Object> map = new HashMap<>();
        map.put("host Id", hostId);
        return findResults( "Feed.findByHostId", map);       
    }
    
        public Feed findByPath(String path){
          Map<String, Object> map = new HashMap<>();
        map.put("path", path);
        return findResult( "Feed.findByPath", map);       
    }
        
    public List<Feed> findContaining (String search){
        Map<String, Object> map = new HashMap<>();
        map.put("search", search);
        return findResults( "Feed.findContaining", map);
    }
  
}
