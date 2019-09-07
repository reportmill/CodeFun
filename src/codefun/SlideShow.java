package codefun;
import java.util.*;
import snap.gfx.*;
import snap.web.WebURL;

/**
 * A class to hold SlideNodes.
 */
public class SlideShow {
    
    // The SlidePane
    SlidePane           _slidePane;
    
    // The source URL
    WebURL              _srcURL, _parURL;
    
    // The leading list of directives
    List <SlideNode>    _directives = new ArrayList();
    
    // The list of SlideNodes
    List <SlideNode>    _slideNodes = new ArrayList();
    
    // The background image
    Image               _backImg;
    
    // The decoration image
    Image               _img;

    // The transition
    Transition          _transition = Transition.SlideLeft;
    
    // Transitions
    public enum Transition { SlideLeft, SlideRight, SlideDown, SlideUp, FadeIn, Explode, Instant };
    
/**
 * Creates a new SlideShow for source.
 */
public SlideShow(SlidePane aSP, Object aSource)
{
    _slidePane = aSP;
    setSource(aSource);
    
    if(_img==null) _img = Image.getImageForSize(5,5,true);
    if(_backImg==null) {
        _backImg = Image.getImageForSize(792,612,false);
        Painter pntr = _backImg.getPainter();
        pntr.setPaint(Color.PINK); pntr.fillRect(0,0,_backImg.getWidth(), _backImg.getHeight());
    }
}

/**
 * Returns the SlidePane.
 */
public SlidePane getSlidePane()  { return _slidePane; }

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
    
    String text = _srcURL.getText();
    text = text.replace("\n\n", "\n").replace("\n\n", "\n");
    String items[] = text.split("\n");
    
    int i = 0; while(i<items.length && items[i].trim().length()==0) i++;
    while(i<items.length) {
        int start = i, end = i+1; while(end<items.length && items[end].startsWith("\t")) end++;
        String items2[] = Arrays.copyOfRange(items, start, end);
        SlideNode snode = new SlideNode(this, items2);
        _slideNodes.add(snode);
        i = end; while(i<items.length && items[i].trim().length()==0) i++;
    }
    
    // Walk through nodes and move directives to slides or show
    SlideNode slide = null;
    SlideNode nodes[] = _slideNodes.toArray(new SlideNode[0]);
    for(SlideNode node : nodes) {
        if(node.isDirective()) {
            if(slide==null) _directives.add(node);
            else slide._directives.add(node);
            _slideNodes.remove(node);
        }
    }
    
    // Process directives
    processDirectives();
}

/**
 * Returns the number of slides.
 */
public int getSlideCount()  { return _slideNodes.size(); }

/**
 * Returns the individual slide at given index.
 */
public SlideNode getSlide(int anIndex)  { return _slideNodes.get(anIndex); }

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
 * Processes a directives.
 */
protected void processDirectives()
{
    for(SlideNode node : _directives)
        processDirective(node);
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