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
    
    // Whether player is moveing to previous slide
    boolean             _reversing;
    
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
    // If already set, just return
    if(anIndex<0 || anIndex>=getSlideCount()) return;
    
    // If reversing, get transition
    SlideShow.Transition revTrans = null;
    if(_reversing) {
        SlideNode slide = _slideShow.getSlide(_sindex);
        revTrans = slide.getTransition();
        revTrans = SlideUtils.getTransitionReverse(revTrans);
    }
    
    // Set value
    _sindex = anIndex;
    
    // Reset transition
    _slideShow.setTransition(SlideShow.Transition.SlideLeft);
    
    // Process directives for slide
    SlideNode slide = getSlide(anIndex);
    if(slide!=null)
        slide.processDirectives();
        
    // If reversing, reverse transition
    if(_reversing)
        _slideShow.setTransition(revTrans);
    
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
        case Construct: configureConstruct(aSV); return;
    }

    // Reset content
    _mainBox.setContent(aSV);
}

/**
 * Sets the next slide.
 */
public void nextSlide()
{
    // If no previous slide, complain and return
    int index = getSlideIndex()+1;
    if(index>=getSlideCount()) {
        beep(); return; }
    
    // Set slide index
    setSlideIndex(index);
}

/**
 * Sets the previous slide.
 */
public void prevSlide()
{
    // If no previous slide, complain and return
    int index = getSlideIndex()-1;
    if(index<0) {
        beep(); return; }
        
    // Set slide
    _reversing = true;
    setSlideIndex(index);
    _reversing = false;
}

/**
 * Configures explode transition.
 */
protected void configureExplode()
{
    View view = _mainBox.getContent();
    if(view!=null) {
        _mainBox.setTransition(TransitionPane.FadeIn);
        new Explode(view, 20, 20, null).setHostView(_mainBox.getParent()).playAndRestore();
    }
    
    else _mainBox.setTransition(TransitionPane.MoveRight);
}

/**
 * Configures construct transition.
 */
protected void configureConstruct(SlideView aView)
{
    if(aView==null) return;
    ViewUtils.addChild(_mainBox, aView);
    new Explode(aView, 20, 20, () -> constructDone(aView)).setHostView(_mainBox.getParent()).reverse().playAndRestore();
    ViewUtils.removeChild(_mainBox, aView);
}

/** Called when the Construct animation is finished to actually add new view. */
private void constructDone(View aView)
{
    aView.setOpacity(1);
    _mainBox.setTransition(TransitionPane.Instant);
    _mainBox.setContent(aView);
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