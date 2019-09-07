package codefun;
import snap.gfx.*;
import snap.util.SnapUtils;
import snap.view.*;
import snap.web.WebURL;

/**
 * The main class for Slides.
 */
public class SlideApp extends ViewOwner {
    
    // The main box view
    BoxView               _mainBox;
    
    // The OpenPanel view
    View                  _openPanelView;
    
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
    
    // Get OpenPanelView
    _openPanelView = _mainBox.getContent();
    
    // Get ListView
    _listView = getView("ListView", ListView.class);
    String names[] = getShowNames();
    _listView.setItems(names);
    _listView.setSelIndex(0);
    enableEvents(_listView, MouseRelease);
    
    // Decorate open box
    View openBox = _listView.getParent().getParent();
    openBox.setPadding(30,30,30,30);
    openBox.setFill(ViewUtils.getBackFill());
    openBox.setBorder(Color.DARKGRAY, 2);
    
    // Add action to bring back open panel when escape key hit
    addKeyActionFilter("EscapeAction", "ESCAPE");
}

/**
 * Respond to UI.
 */
protected void respondUI(ViewEvent anEvent)
{
    // Handle OpenButton
    if(anEvent.equals("ListView") && anEvent.getClickCount()==2)
        openSelectedShow();
    
    // Handle OpenButton
    if(anEvent.equals("OpenButton"))
        openSelectedShow();
        
    // Handle SourceButton
    if(anEvent.equals("SourceButton"))
        openSelectedShowSource();
        
    // Handle EscapeAction
    if(anEvent.equals("EscapeAction"))
        showOpenPanel();
}

/**
 * Shows the open panel.
 */
public void showOpenPanel()
{
    _mainBox.setContent(_openPanelView);
}

/**
 * Opens selected show.
 */
public void openSelectedShow()
{
    // Get URL for SlideShow
    String name = _listView.getSelItem();
    String path = ROOT + '/' + name + "/Slides.txt";
    WebURL url = WebURL.getURL(path);
    
    // Create SlideShow
    SlideShow show = new SlideShow(url);
    
    // Create SlidePlayer
    SlidePlayer player = new SlidePlayer(show);
    
    // Install in MainBox
    _mainBox.setContent(player.getUI());
}

/**
 * Opens selected show.
 */
public void openSelectedShowSource()
{
    // Get URL for SlideShow
    String name = _listView.getSelItem();
    String path = ROOT + '/' + name + "/Slides.txt";
    WebURL url = WebURL.getURL(path);
    
    // Create SlideShow
    String text = url.getText();
    TextView textView = new TextView();
    textView.setText(text);
    textView.setFont(new Font("Arial", 14));
    
    // Wrap in Box
    BoxView boxView = new BoxView(textView, true, true);
    boxView.setGrowWidth(true); boxView.setGrowHeight(true);
    boxView.setPadding(12,12,12,12);
    boxView.setFill(ViewUtils.getBackFill());
    
    // Install in MainBox
    _mainBox.setContent(boxView);
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
    snaptea.TV.set();
    if(SnapUtils.isTeaVM)
        ROOT = "http://reportmill.com/snaptea/SlideShows";
    
    SlideApp app = new SlideApp();
    
    if(SnapUtils.isTeaVM)
        app.getWindow().setMaximized(true);
    app.setWindowVisible(true);
}

}