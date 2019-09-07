package codefun;
import snap.gfx.*;
import snap.view.*;
import snap.web.WebURL;

/**
 * A view to render a slide.
 */
public class SlideView extends ChildView {
    
    // The slide
    SlideNode         _slide;

    // The SlideShow
    SlideShow         _slideShow;
    
    // The background
    ImageView         _backView;
    
    // The header view
    TextArea          _headerView;
    
    // The body view
    ColView           _bodyView;
    
    // The page text
    StringView        _pageText;
    
    // The fonts
    Font              _font0 = Font.Arial14.deriveFont(64).getBold();
    Font              _font1 = Font.Arial14.deriveFont(36);
    Font              _font2 = Font.Arial14.deriveFont(20);
    Color             _color = new Color(40,0,0,200); //new Color(245,245,255);

/**
 * Creates SlideView for given slide.
 */ 
public SlideView(SlideNode aSlide)
{
    // Set slide and slideshow
    _slide = aSlide;
    _slideShow = _slide.getSlideShow();
    
    // Configure basic view attributes
    setAlign(VPos.CENTER); setPrefSize(792,612); setSize(792,612); setBorder(Color.BLACK,1);
    enableEvents(MouseRelease);
    
    // Create background image view
    ImageView backImgView = new ImageView(_slideShow._backImg, true, true);
    backImgView.setBounds(1,1,790,610);
    addChild(backImgView);
    
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
    _bodyView = new ColView(); _bodyView.setAlign(VPos.CENTER);
    _bodyView.setSpacing(12); //_bodyView.setFillWidth(true);
    _bodyView.setBounds(72,150,684,400);
    addChild(_bodyView);
    
    // Create PageText
    _pageText = new StringView(); _pageText.setAlign(Pos.CENTER); _pageText.setTextFill(Color.WHITE);
    _pageText.setBounds(350,580,92,20);
    addChild(_pageText);
    
    // Set PageText text
    int pageNum = _slide.getPageNum();
    int pageCount = _slideShow.getSlideCount();
    _pageText.setText(pageNum + " of " + pageCount);
    
    // Create badge
    ImageView iview = new ImageView(_slideShow._img); iview.setSize(iview.getPrefSize());
    iview.setXY(getWidth() - iview.getWidth() - 10, getHeight() - iview.getHeight() - 10);
    iview.setEffect(new ShadowEffect(6,Color.BLACK,1,1)); iview.setFill(Color.CLEAR);
    addChild(iview);
    
    // Set HeaderText
    String headerText = _slide.getText();
    setHeaderText(headerText);
    
    // Add items
    for(SlideNode node : _slide.getItems())
        addItem(node);
        
    // Shrink fonts to fit
    shrinkItemFontsToFit();
}

/**
 * Returns the SlideShow.
 */
public SlideShow getSlideShow()  { return _slideShow; }

/**
 * Returns the SlidePlayer.
 */
public SlidePlayer getPlayer()  { return _slideShow.getPlayer(); }

/**
 * Returns the page number.
 */
public int getPageNum()  { return _slide.getPageNum(); }

/**
 * Sets the HeaderText.
 */
public void setHeaderText(String aValue)
{
    _headerView.setText(aValue);
    _headerView.getTextBox().scaleTextToFit();
}

/**
 * Add an item.
 */
public void addItem(SlideNode aNode)
{
    // Get directive
    String dir = aNode.getDirective();
    if(dir==null) {
        addTextItem(aNode); return; }
        
    // Handle directives
    switch(dir) {
        case "Image": addImageItem(aNode); break;
        case "Hide": break;
        default: addTextItem(aNode); break;
    }
}

/**
 * Add a normal text item.
 */
public void addTextItem(SlideNode aNode)
{
    // Get text with bullet char prefix
    String text = aNode.getText();
    if(text.startsWith("\t\t")) text = text.replace("\t\t", "\t\u2022 ");
    else if(text.startsWith("\t")) text = text.replace("\t", "\u2022 ");
    
    // Create TextArea
    TextArea textView = new TextArea(); textView.setGrowWidth(true);
    textView.setText(text); textView.setEditable(false); textView.setWrapText(true);
    textView.setFill(null); textView.setTextFill(Color.WHITE); textView.setFont(_font1);
    textView.addEventHandler(e -> textView.setFill(_color), MouseEnter);
    textView.addEventHandler(e -> textView.setFill(null), MouseExit);
    
    // Add to body view
    _bodyView.addChild(textView);
}

/**
 * Add a normal text item.
 */
public void addImageItem(SlideNode aNode)
{
    // Get source
    String src = aNode.getDirectiveValue("Src");
    if(src==null) { System.out.println("SlideView.addImageItem: No source defined"); return; }
    
    // Get image
    WebURL url = getSlideShow()._parURL.getChild(src);
    Image img = Image.get(url);
    if(img==null) { System.out.println("SlideView.addImageItem: Image not found for source" + src); return; }
    
    // Create ImageView
    ImageView iview = new ImageView(img, true, true);
    iview.setLeanX(HPos.CENTER);
    
    // Add to BodyView
    _bodyView.addChild(iview);
}

protected void shrinkItemFontsToFit()
{
    // While body view wants to grow off page, shrink item text fonts
    double scale = 1;
    while(_bodyView.getPrefHeight(_bodyView.getWidth())>_bodyView.getHeight()) {
        
        // Iterate over children
        System.out.println("SlideView.shrinkItemFontsToFit: " + _bodyView.getPrefHeight(_bodyView.getWidth()));
        scale -= .05;
        for(View child : _bodyView.getChildren()) {
        
            // Handle TextArea
            if(child instanceof TextArea) { TextArea text = (TextArea)child;
                text.setFontScale(scale);
            }
            
            // Handle anything else
            else {
                child.setPrefSize(-1,-1);
                Size size = child.getPrefSize();
                size.width *= scale; size.height *= scale;
                child.setPrefSize(size);
            }
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
        if(anEvent.getX()>getWidth()/3) getPlayer().nextSlide();
        else getPlayer().prevSlide();
    }
}

}