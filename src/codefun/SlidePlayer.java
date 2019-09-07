package codefun;
import snap.gfx.Color;
import snap.view.*;
import snap.viewx.TransitionPane;

/**
 * A class to play a SlideShow (manage views, clicks and such).
 */
public class SlidePlayer extends ViewOwner {

    // The SlideShow
    SlideShow           _slideShow;
    
    // The current slide index
    int                 _sindex = -1;
    
    // The Slide box
    TransitionPane      _mainBox;
    
/**
 * Creates a new SlidePlayer for SlideShow.
 */
public SlidePlayer(SlideShow aShow)
{
    // Set the SlideShow and set SlideShow.Player to this
    _slideShow = aShow;
    _slideShow._player = this;
    
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
public SlideNode getSlide(int anIndex)  { return _slideShow.getSlide(anIndex); }

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
    
    // Process directives for slide
    SlideNode slide = getSlide(anIndex);
    if(slide!=null)
        slide.processDirectives();
    
    // Install slide view
    SlideView sview = getSlideView(anIndex);
    setSlideView(sview);
}

/**
 * Sets the current slide view.
 */
public void setSlideView(SlideView aSV)
{
    // Configure transiton
    SlideShow.Transition trans = _slideShow.getTransition();
    switch(trans) {
        case SlideLeft: _mainBox.setTransition(TransitionPane.MoveRight); break;
        case SlideRight: _mainBox.setTransition(TransitionPane.MoveLeft); break;
        case SlideUp: _mainBox.setTransition(TransitionPane.MoveUp); break;
        case SlideDown: _mainBox.setTransition(TransitionPane.MoveDown); break;
        case FadeIn: _mainBox.setTransition(TransitionPane.FadeIn); break;
        case Instant: _mainBox.setTransition(TransitionPane.Instant); break;
        case Explode: configureExplode(); break;
    }

    // Reset content
    _mainBox.setContent(aSV);
}

/**
 * Sets the next slide.
 */
public void nextSlide()
{
    setSlideIndex(getSlideIndex()+1);
}

/**
 * Sets the previous slide.
 */
public void prevSlide()
{
    // Configure reverse transition
    SlideShow.Transition trans1 = _slideShow.getTransition();
    SlideShow.Transition trans2 = _slideShow.getTransitionReverse();
    _slideShow.setTransition(trans2);
    
    // Set slide
    setSlideIndex(getSlideIndex()-1);
    
    // Restore transition
    _slideShow.setTransition(trans1);
}

/**
 * Configures explode transition.
 */
protected void configureExplode()
{
    View view = _mainBox.getContent();
    if(view!=null) {
        _mainBox.setTransition(TransitionPane.FadeIn);
        new Explode(view, 30, 30, null).setHostView(_mainBox.getParent()).playAndRestore();
    }
    
    else _mainBox.setTransition(TransitionPane.MoveRight);
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

}