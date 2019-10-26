/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import dao.FeedDAO;
import entity.Feed;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
 

/**
 *
 * @author Lanre
 */
public class FeedLogic extends GenericLogic<Feed, FeedDAO> {

    public static final String ID = "id";
    public static final String PATH = "path";
    public static final String TYPE = "type";
    public static final String HOST_ID = "hostid";
    public static final String NAME = "name";

    public FeedLogic() {
        super(new FeedDAO());
    }

    @Override
    public List getAll() {
        return get(() -> dao().findAll());
    }

    @Override
    public Feed getWithId(int id) {
        return get(() -> dao().findById(id));
    }

    public List<Feed> getFeedsWithHostId(int hostId) {
        return get(() -> dao().findByHostId(hostId));
    }

    public Feed getFeedsWithPath(String path) {
        return get(() -> dao().findByPath(path));
    }

    public List<Feed> getFeedsWithType(String type) {
        return get(() -> dao().findByType(type));
    }

    public List<Feed> getFeedsWithName(String name) {
        return get(() -> dao().findByName(name));
    }

    @Override
    public List<Feed> search(String search) {
        return get(() -> dao().findContaining(search));
    }

    @Override
    public Feed createEntity(Map<String, String[]> parameterMap) {
        Feed feed = new Feed();
        if (parameterMap.containsKey(ID)) {

            feed.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }
        if (parameterMap.containsKey(NAME) && parameterMap.get(NAME) != null) {
            String name = parameterMap.get(NAME)[0];
            if (name.isEmpty() || name.length() > 45) {
                throw new RuntimeException("Invalid Parameter Lenght");
            }
        } else {
            throw new RuntimeException("Host name must be available");
        }

//   feed.setHostid(parameterMap.get(HOST_ID)[0]);
        feed.setPath(parameterMap.get(PATH)[0]);
        feed.setType(parameterMap.get(TYPE)[0]);
        feed.setName(parameterMap.get(NAME)[0]);
        return feed;
    }

    @Override
    public List getColumnNames() {
        return Arrays.asList("ID", "PATH", "TYPE", "NAME", "HOST ID");
    }

    @Override
    public List getColumnCodes() {
        return Arrays.asList(ID, PATH, TYPE, NAME, HOST_ID);
    }

    @Override
    public List<?> extractDataAsList(Feed e) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return Arrays.asList(e.getId(), e.getHostid(), e.getPath(), e.getHostid(), e.getType(), e.getName());
    }
}
