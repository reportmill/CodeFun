package codefun;
import snap.gfx.Color;
import snap.view.*;
import snap.web.WebURL;

/**
 * The main class for Slides.
 */
public class SlideApp extends ViewOwner {
    
    // The main box view
    BoxView               _mainBox;
    
    // The ListView
    ListView <String>     _listView;
    
    // The SlideShows
    String                _showNames[];

    // The root
    private static String ROOT = "/Temp/SlideShows";
    
/**
 * Initialize UI.
 */
protected void initUI()
{
    // Get MainBox
    _mainBox = (BoxView)getUI();
    _mainBox.setFill(Color.WHITE);
    _mainBox.setPrefSize(792,612);
    
    // Get ListView
    _listView = getView("ListView", ListView.class);
    String names[] = getShowNames();
    _listView.setItems(names);
    _listView.setSelIndex(0);
    
    // Decorate open box
    View openBox = _listView.getParent().getParent();
    openBox.setPadding(30,30,30,30);
    openBox.setFill(new Color("#B9F7F5"));
    openBox.setBorder(Color.DARKGRAY, 2);
}

/**
 * Respond to UI.
 */
protected void respondUI(ViewEvent anEvent)
{
    // Handle OpenButton
    if(anEvent.equals("OpenButton")) {
        String name = _listView.getSelItem();
        SlidePane spane = new SlidePane();
        String path = ROOT + '/' + name + "/Slides.txt";
        WebURL url = WebURL.getURL(path);
        spane.setSource(url);
        _mainBox.setContent(spane.getUI());
    }
        
}

/**
 * Returns the list of shows.
 */
public String[] getShowNames()
{
    // If already set, just return
    if(_showNames!=null) return _showNames;
    
    // Load names
    String path = ROOT + "/AllShows.txt";
    WebURL url = WebURL.getURL(path);
    
    // Load names
    String indexText = url.getText();
    String names[] = indexText.split("\n");
    for(int i=0;i<names.length;i++) names[i] = names[i].trim();
    return _showNames = names;
}

/**
 * Standard main method.
 */
public static void main(String args[])
{
    SlideApp app = new SlideApp();
    
    app.setWindowVisible(true);
}

}