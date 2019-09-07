package codefun;
import java.util.*;
import snap.gfx.*;
import snap.web.WebURL;

/**
 * A class to hold Slides.
 */
public class SlideShow {
    
    // The source URL
    WebURL              _srcURL, _parURL;
    
    // The list of SlideNodes
    List <SlideNode>    _slides = new ArrayList();
    
    // The background image
    Image               _backImg;
    
    // The decoration image
    Image               _img;

    // The transition
    Transition          _transition = Transition.SlideLeft;
    
    // The Player
    SlidePlayer         _player;
    
    // Transitions
    public enum Transition { SlideLeft, SlideRight, SlideDown, SlideUp, FadeIn, Explode, Instant };
    
/**
 * Creates a new SlideShow for source.
 */
public SlideShow(Object aSource)
{
    // Set the show source
    setSource(aSource);
    
    // Set background/image defaults if not set
    if(_img==null) _img = Image.getImageForSize(5,5,true);
    if(_backImg==null) {
        _backImg = Image.getImageForSize(792,612,false);
        Painter pntr = _backImg.getPainter();
        pntr.setPaint(Color.PINK); pntr.fillRect(0,0,_backImg.getWidth(), _backImg.getHeight());
    }
}

/**
 * Returns the SlidePlayer.
 */
public SlidePlayer getPlayer()  { return _player; }

/**
 * Sets the slides from source.
 */
public void setSource(Object aSource)
{
    // Get Source URL
    _srcURL = WebURL.getURL(aSource);
    if(_srcURL==null) {
        System.err.println("SlideShow.setSource: Can't find source URL for " + aSource); return; }
    _parURL = _srcURL.getParent();
    
    // Get Show text
    String text = _srcURL.getText();
    setText(text);
}

/**
 * Sets the slides from text.
 */
public void setText(String aString)
{
    // Get Show text
    String text = aString;
    
    // Get items as strings with no white space
    text = text.replace("\n\n", "\n").replace("\n\n", "\n");
    String items[] = text.split("\n");
    int i = 0; while(i<items.length && items[i].trim().length()==0) i++;
    
    // Iterate over items to create nodes
    SlideNode slide = null;
    List <SlideNode> directives = new ArrayList();
    for(String itemStr : items) {
        
        // If empty slide, just skip
        if(itemStr.trim().length()==0) continue;
        
        // Ignore tabs before first slide
        if(slide==null && itemStr.startsWith("\t")) {
            itemStr = itemStr.trim();
            System.out.println("SlideShow.setSource: Indented line before first slide");
        }
    
        // Create new node for item string
        SlideNode node = new SlideNode(this, itemStr);
        
        // If indented, add to last slide
        if(itemStr.startsWith("\t"))
            slide.addItem(node);
            
        // If directive, add to directives list
        else if(node.isDirective())
            directives.add(node);
            
        // Otherwise create new slide
        else {
            slide = node;
            for(SlideNode dir : directives)
                slide._directives.add(dir);
            directives.clear();
            _slides.add(slide);
        }
    }
}

/**
 * Returns the number of slides.
 */
public int getSlideCount()  { return _slides.size(); }

/**
 * Returns the individual slide at given index.
 */
public SlideNode getSlide(int anIndex)  { return _slides.get(anIndex); }

/**
 * Returns the individual slide at given index.
 */
public SlideView getSlideView(int anIndex)  { return getSlide(anIndex).getSlideView(); }

/**
 * Returns the transition.
 */
public Transition getTransition()  { return _transition; }

/**
 * Sets the transition.
 */
public void setTransition(Transition aTrans)  { _transition = aTrans; }

/**
 * Returns the reverse transition.
 */
public Transition getTransitionReverse()
{
    return Transition.SlideRight;
}

/**
 * Processes a directive.
 */
protected void processDirective(SlideNode aNode)
{
    // Get Directive
    String dir = aNode.getDirective();
    
    // Handle Background
    if(dir.equals("Background")) {
        String src = aNode.getDirectiveValue("Src");
        if(src!=null) {
            WebURL url = _parURL.getChild(src);
            _backImg = Image.get(url);
        }
    }
    
    // Handle Decoration
    else if(dir.equals("Decoration")) {
        String src = aNode.getDirectiveValue("Src");
        if(src!=null) {
            WebURL url = _parURL.getChild(src);
            _img = Image.get(url);
        }
    }
    
    // Handle Transition
    else if(dir.equals("Transition")) {
        String name = aNode.getDirectiveValue("Name");
        try {
            Transition trans = Transition.valueOf(name);
            setTransition(trans);
        }
        catch(IllegalArgumentException e) {
            System.err.println("SlideShow.processDirective: Unknown Transition: " + name);
        }
    }
    
    // Otherwise complain
    else System.err.println("SlideShow.processDirectives: Unknown directive: " + dir);
}

}