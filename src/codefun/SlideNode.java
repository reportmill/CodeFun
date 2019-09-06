package codefun;
import java.util.*;

/**
 * A class to describe a slide or a slide directive.
 */
public class SlideNode {
    
    // The SlideShow
    SlideShow            _show;
    
    // The text for this node
    String               _text;
    
    // The items
    SlideNode            _items[];
    
    // The directive
    String               _directive;
    
    // The directive values
    Map <String,String>  _dirVals;
    
    // The leading list of directives
    List <SlideNode>     _directives = new ArrayList();
    
    // The SlideView
    SlideView            _slideView;
    
/**
 * Creates a SlideNode for given Show and items.
 */
public SlideNode(SlideShow aShow, String aStr)
{
    _show = aShow;
    _text = aStr;
}

/**
 * Creates a SlideNode for given Show and items.
 */
public SlideNode(SlideShow aShow, String theItems[])
{
    this(aShow, theItems[0]);
    
    _items = new SlideNode[theItems.length-1];
    for(int i=1;i<theItems.length;i++)
        _items[i-1] = new SlideNode(aShow, theItems[i]);
}

/**
 * Returns the SlideShow.
 */
public SlideShow getSlideShow()  { return _show; }

/**
 * Returns the node text.
 */
public String getText()  { return _text; }

/**
 * Returns the node items.
 */
public SlideNode[] getItems()  { return _items; }

/**
 * Returns the number of items.
 */
public int getItemCount()  { return _items.length; }

/**
 * Returns the individual item.
 */
public SlideNode getItem(int anIndex)  { return _items[anIndex]; }

/**
 * Returns whether node is a directive.
 */
public boolean isDirective()  { return getDirective()!=null; }

/**
 * Returns the directive.
 */
public String getDirective()
{
    // If already set, just return (empty string returns as null)
    if(_directive!=null) return _directive!=""? _directive : null;
    
    // Get item text and return null if no directive
    String text = getText().trim();
    if(!text.startsWith("[")) { _directive = ""; return null; }
    int end = text.indexOf("]");
    if(end<0) { _directive = ""; return null; }
        
    // Otherwise get/set directive
    _directive = text.substring(1, end);
    
    // Get directive values
    String suffix = text.substring(_directive.length()+2);
    String entryStrings[] = suffix.split(",");
    _dirVals = new HashMap();
    
    /// Iterate over entry strings and add to DirVals map
    for(String entryStr : entryStrings) { entryStr = entryStr.trim();
        String parts[] = entryStr.split(":");
        if(parts.length==2) {
            _dirVals.put(parts[0], parts[1]);
        }
    }
    
    // Return directive
    return _directive;
}

/**
 * Returns the directive.
 */
public String getDirectiveValue(String aKey)
{
    return _dirVals!=null? _dirVals.get(aKey) : null;
}

/**
 * Returns the page number.
 */
public int getPageNum()  { return (_show._slideNodes.indexOf(this) + 1); }

/**
 * Returns the SlideView.
 */
public SlideView getSlideView()
{
    if(_slideView!=null) return _slideView;
    
    SlideView sview = new SlideView(this);
    return _slideView = sview;
}

}