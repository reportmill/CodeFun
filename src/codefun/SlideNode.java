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
    SlideNode            _items[] = new SlideNode[0];
    
    // The directive (if slide is directive)
    String               _directive;
    
    // The directive values (if slide is directive)
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
 * Adds a slide item.
 */
public void addItem(SlideNode aNode)
{
    int len = _items.length;
    _items = Arrays.copyOf(_items, len+1);
    _items[len] = aNode;
}

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
 * Returns whether slide is title slide.
 */
public boolean isTitleSlide()
{
    String type = getSlideType();
    return type.equals("Title");
}

/**
 * Returns the slide type.
 * First slide or slides without children are type "Title".
 */
public String getSlideType()
{
    // Iterate over directives to find transition
    for(SlideNode dir : _directives) {
        if(dir.getDirective().equals("SlideType")) {
            String name = dir.getDirectiveValue("Name");
            if(name!=null)
                return name;
        }
    }
    
    if(getPageNum()==1 || getItemCount()==0)
        return "Title";
    return "Basic";
}

/**
 * Returns the transition this slide uses.
 */
public SlideShow.Transition getTransition()
{
    // Iterate over directives to find transition
    for(SlideNode dir : _directives) {
        if(dir.getDirective().equals("Transition")) {
            String name = dir.getDirectiveValue("Name");
            SlideShow.Transition trans = SlideUtils.getTransitionForName(name);
            if(trans!=null)
                return trans;
        }
    }
    
    // If no Transition directive, return default (SlideLeft)
    return SlideShow.Transition.SlideLeft;
}

/**
 * Returns the page number.
 */
public int getPageNum()  { return (_show._slides.indexOf(this) + 1); }

/**
 * Returns the max letter count of items.
 */
public int getMaxItemLength()
{
    int len = 0;
    for(SlideNode item : getItems()) {
        if(item.isDirective()) continue;
        len = Math.max(len, item.getText().length());
    }
    return len;
}

/**
 * Processes a directives.
 */
public void processDirectives()
{
    for(SlideNode node : _directives)
        processDirective(node);
}

/**
 * Processes a directive.
 */
protected void processDirective(SlideNode aNode)
{
    // For now, just send to show
    _show.processDirective(aNode);
}

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