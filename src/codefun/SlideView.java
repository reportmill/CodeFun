package codefun;
import snap.gfx.*;
import snap.view.*;

/**
 * A view to show a slide.
 */
public class SlideView extends ChildView {
    
    // The SlideNode
    SlideNode         _snode;

    // The SlideShow
    SlideShow         _slideShow;
    
    // The background
    ImageView         _backView;
    
    // The header view
    TextArea          _headerView;
    
    // The body view
    BodyView          _bodyView;
    
    // The page text
    StringView        _pageText;
    
    // The fonts
    Font              _font0 = Font.Arial14.deriveFont(64).getBold();
    Font              _font1 = Font.Arial14.deriveFont(36);
    Font              _font2 = Font.Arial14.deriveFont(20);
    Color             _color = new Color(40,0,0,200); //new Color(245,245,255);

/**
 * Creates a new SlideView for given SlidePane.
 */ 
public SlideView(SlideNode aSNode)
{
    _snode = aSNode;
    _slideShow = _snode.getSlideShow();
    
    setAlign(VPos.CENTER); setPrefSize(792,612); setSize(792,612); setBorder(Color.BLACK,1);
    enableEvents(MouseRelease);
    
    // Create background image view
    ImageView backImgView = new ImageView(_slideShow._backImg, true, true);
    backImgView.setBounds(1,1,790,610);
    addChild(backImgView);
    
    // Create header rect
    //RectView rview = new RectView(); rview.setFill(Color.LIGHTBLUE); rview.setBounds(36,18,720,150);
    //addChild(rview);
    
    // Create HeaderView
    _headerView = new TextArea(); _headerView.setAlign(VPos.CENTER); _headerView.setWrapText(true);
    TextLineStyle lstyle = _headerView.getRichText().getDefaultLineStyle().copyFor(HPos.CENTER);
    _headerView.getRichText().setDefaultLineStyle(lstyle);
    TextLineStyle lstyle2 = lstyle;
    _headerView.setFont(_font0);
    _headerView.setFill(Color.CLEAR);
    _headerView.setTextFill(Color.WHITE); //_headerView.setEffect(new ShadowEffect(12,new Color(180,0,0),0,0));
    _headerView.setPadding(16,16,16,16);
    _headerView.setBounds(36,18,720,130);
    addChild(_headerView);
    
    // Create BodyView
    _bodyView = new BodyView(); _bodyView.setAlign(VPos.CENTER);
    _bodyView.setBounds(72,150,684,400);
    addChild(_bodyView);
    
    // Create PageText
    _pageText = new StringView(); _pageText.setAlign(Pos.CENTER); _pageText.setTextFill(Color.WHITE);
    _pageText.setBounds(350,580,92,20);
    addChild(_pageText);
    
    // Create badge
    ImageView iview = new ImageView(_slideShow._img); iview.setSize(iview.getPrefSize());
    iview.setXY(getWidth() - iview.getWidth() - 10, getHeight() - iview.getHeight() - 10);
    iview.setEffect(new ShadowEffect(6,Color.BLACK,1,1)); iview.setFill(Color.CLEAR);
    addChild(iview);
}

/**
 * Returns the SlideShow.
 */
public SlideShow getSlideShow()  { return _slideShow; }

/**
 * Returns the SlidePane.
 */
public SlidePane getSlidePane()  { return _slideShow.getSlidePane(); }

/**
 * Returns the page number.
 */
public int getPageNum()  { return _snode.getPageNum(); }

/**
 * Sets the HeaderText.
 */
public void setHeaderText(String aValue)
{
    _headerView.setText(aValue);
    _headerView.getTextBox().scaleTextToFit();
}

/**
 * Sets the slide text items.
 */
public void setItems(String theItems[])
{
    // Set HeaderText
    if(theItems.length>0)
        setHeaderText(theItems[0]);
        
    // Add Body items
    for(int i=1;i<theItems.length;i++) { String item = theItems[i];
        if(item.trim().length()==0) continue;
        _bodyView.addItem(item);
    }
    
    while(_bodyView.getPrefHeight(_bodyView.getWidth())>_bodyView.getHeight()) {
        System.out.println("PW: " + _bodyView.getPrefHeight(_bodyView.getWidth()));
        for(View child : _bodyView.getChildren()) { TextArea text = (TextArea)child;
            double scale = text.getFontScale() - .05;
            text.setFontScale(scale);
        }
        _bodyView.relayout();
    }
}

/**
 * Handle events.
 */
protected void processEvent(ViewEvent anEvent)
{
    // Handle mouse click
    if(anEvent.isMouseClick()) {
        if(anEvent.getX()>getWidth()/3) getSlidePane().nextSlide();
        else getSlidePane().prevSlide();
    }
}

/**
 * Override to set page number.
 */
protected void setParent(ParentView aPar)
{
    super.setParent(aPar); if(aPar==null) return;
    _pageText.setText(getPageNum() + " of " + _slideShow.getSlideCount());
}

/**
 * The view for the body.
 */
public class BodyView extends ColView {
    
    /** Creates a new BodyView. */
    public BodyView() { setSpacing(12); setFillWidth(true); }
    
    /**
     * Adds a new item.
     */
    public void addItem(String anItem)
    {
        // Get string
        String string = anItem;
        if(string.startsWith("\t\t")) string = string.replace("\t\t", "\t\u2022 ");
        else if(string.startsWith("\t")) string = string.replace("\t", "\u2022 ");
        
        // Create TextArea
        TextArea text = new TextArea(); text.setFont(_font1); text.setWrapText(true); text.setTextFill(Color.WHITE);
        text.setText(string); text.setEditable(false); text.setFill(null);
        text.addEventHandler(e -> text.setFill(_color), MouseEnter);
        text.addEventHandler(e -> text.setFill(null), MouseExit);
        addChild(text);
    }
}

}