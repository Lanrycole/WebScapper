package logic;

import dao.ImageDAO;
import entity.Image;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shariar
 */
public class ImageLogic extends GenericLogic<Image, ImageDAO> {

    public static final String ID = "id";
    public static final String PATH = "path";
    public static final String NAME = "name";
    public static final String DATE = "date";
    public static final String FEEDID = "feedId";

    public ImageLogic() {
        super(new ImageDAO());
    }

    @Override
    public List<Image> getAll() {
        return get(() -> dao().findAll());
    }

    @Override
    public Image getWithId(int id) {
        return get(() -> dao().findById(id));
    }

    public List<Image> getImagesWithFeedId(int feedId) {
        return get(() -> dao().findByFeedId(feedId));
    }

    public List<Image> getImagesWithName(String name) {
        return get(() -> dao().findByName(name));
    }

    public Image getImagesWithPath(String path) {
        return get(() -> dao().findByPath(path));
    }

    public List<Image> getImagesWithDate(Date date) {
        return get(() -> dao().findByDate(date));
    }

    //ASK ABOUT FINDCONTAINING
    @Override
    public List<Image> search(String search) {
        return get(() -> dao().findContaining(search));
    }

    @Override
    public Image createEntity(Map<String, String[]> parameterMap) {
        Image image = new Image();

        if (parameterMap.containsKey(ID)) {
            image.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }
        if (parameterMap.containsKey(NAME) && parameterMap.get(NAME) != null) {
            String name = parameterMap.get(NAME)[0];
 
        }
 
        image.setName(parameterMap.get(NAME)[0]);
        image.setPath(parameterMap.get(PATH)[0]);
        image.setDate(new Date(Long.parseLong(parameterMap.get(DATE)[0])));
        return image;

    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Feed Id", "Path", "Date", "Name");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(FEEDID, NAME, PATH, DATE);
    }

    @Override
    public List<?> extractDataAsList(Image e) {
        return Arrays.asList(e.getFeedid(), e.getName(), e.getPath(), e.getDate());
    }

}
