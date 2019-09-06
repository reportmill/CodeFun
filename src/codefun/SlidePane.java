package codefun;
import snap.gfx.Color;
import snap.view.*;
import snap.viewx.TransitionPane;

/**
 * A class to display and manage a list of slides.
 */
public class SlidePane extends ViewOwner {

    // The SlideShow
    SlideShow           _slideShow;
    
    // The current slide index
    int                 _sindex = -1;
    
    // The Slide box
    TransitionPane      _mainBox;
    
/**
 * Sets the slides from source.
 */
public void setSource(Object aSource)
{
    // Load the SlideShow
    _slideShow = new SlideShow(this, aSource);
    
    getUI();
    setSlideIndex(0);
}

/**
 * Returns the number of slides.
 */
public int getSlideCount()  { return _slideShow.getSlideCount(); }

/**
 * Returns the individual slide at given index.
 */
public SlideView getSlideView(int anIndex)  { return _slideShow.getSlideView(anIndex); }

/**
 * Returns the slide index.
 */
public int getSlideIndex()  { return _sindex; }

/**
 * Sets the slide index.
 */
public void setSlideIndex(int anIndex)
{
    if(anIndex<0 || anIndex>=getSlideCount()) return;
    _sindex = anIndex;
    SlideView sview = getSlideView(anIndex);
    setSlideView(sview);
}

/**
 * Sets the current slide view.
 */
public void setSlideView(SlideView aSV)
{
    //double width = getUI().getWidth();
    //if(_mainBox.getContent()!=null) _mainBox.getContent().getAnimCleared(1000).setTransX(-width).play();
    _mainBox.setContent(aSV);
    //aSV.setTransX(width); if(width>0) aSV.getAnimCleared(1000).setTransX(0).play();
}

/**
 * Sets the next slide.
 */
public void nextSlide()
{
    _mainBox.setTransition(TransitionPane.MoveRight);
    setSlideIndex(getSlideIndex()+1);
}

/**
 * Sets the previous slide.
 */
public void prevSlide()
{
    _mainBox.setTransition(TransitionPane.MoveLeft);
    setSlideIndex(getSlideIndex()-1);
}

/**
 * Creates the UI.
 */
protected View createUI()
{
    _mainBox = new TransitionPane(); //_mainBox.setFillWidth(true); _mainBox.setFillHeight(true);
    _mainBox.setFill(Color.WHITE); //_mainBox.setBorder(Color.BLACK,1);
    BoxView box = new ScaleBox(_mainBox, true, true); box.setPadding(4,4,4,4);
    box.setGrowWidth(true); box.setFill(Color.WHITE); //box.setFill(new Color(50,0,0));
    return box;
}

/**
 * Standard main method.
 */
public static void main(String args[])
{
    SlidePane spane = new SlidePane();
    //spane.setSource("/Users/jeff/cpj/Presentation.txt");
    spane.setSource(SlidePane.class.getResource("Slides.txt"));
    spane.setWindowVisible(true);
}

}