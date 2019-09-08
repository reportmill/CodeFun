package codefun;

/**
 * A class to hold some SlideShow utility methods.
 */
public class SlideUtils {

/**
 * Returns a transition for given name.
 */
public static SlideShow.Transition getTransitionForName(String aName)
{
    try { return SlideShow.Transition.valueOf(aName); }
    catch(IllegalArgumentException e) {
        System.err.println("SlideUtils.getTransitionForName: Unknown Transition: " + aName);
        return null;
    }
}

/**
 * Returns the reverse transition.
 */
public static SlideShow.Transition getTransitionReverse(SlideShow.Transition aTrans)
{
    switch(aTrans) {
        case Explode: return SlideShow.Transition.Construct;
        case Construct: return SlideShow.Transition.Explode;
        case SlideLeft: return SlideShow.Transition.SlideRight;
        case SlideRight: return SlideShow.Transition.SlideLeft;
        case SlideUp: return SlideShow.Transition.SlideDown;
        case SlideDown: return SlideShow.Transition.SlideUp;
        case Instant: return SlideShow.Transition.Instant;
        default: return SlideShow.Transition.SlideRight;
    }
}

}