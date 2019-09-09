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
    private static Font    HEADER_FONT = Font.Arial14.deriveFont(64).getBold();
    private static Font    HEADER_FONT_BIG = HEADER_FONT.deriveFont(72);
    private static Font    ITEM_FONT = Font.Arial14.deriveFont(32);
    private static Font    ITEM_FONT_BIG = ITEM_FONT.deriveFont(36);
    private static Font    ITEM_FONT_HUGE = ITEM_FONT.deriveFont(40);
    private static Font    _font2 = Font.Arial14.deriveFont(20);
    private static Color   _color = new Color(40,0,0,200); //new Color(245,245,255);
    private static Effect  SHADOW_EFFECT = new ShadowEffect();
    private static Effect  SHADOW_EFFECT_MORE = new ShadowEffect(15, Color.BLACK, 2, 2);

/**
 * Creates SlideView for given slide.
 */ 
public SlideView(SlideNode aSlide)
{
    // Set slide and slideshow
    _slide = aSlide;
    _slideShow = _slide.getSlideShow();
    
    // Configure basic view attributes
    setAlign(VPos.CENTER);
    setPrefSize(792,612); setSize(792,612);
    setBorder(Color.BLACK,1);
    enableEvents(MouseRelease);
    
    // Create background image view
    ImageView backImgView = new ImageView(_slideShow._backImg, true, true);
    backImgView.setBounds(1,1,790,610);
    addChild(backImgView);
    
    // Create BodyView
    _bodyView = new ColView(); _bodyView.setAlign(VPos.CENTER);
    _bodyView.setSpacing(20); //_bodyView.setFillWidth(true);
    _bodyView.setBounds(72,140,684,400);
    addChild(_bodyView);
    
    // Add HeaderView
    addHeader();
    
    // Create PageText
    _pageText = new StringView(); _pageText.setAlign(Pos.CENTER); _pageText.setTextFill(Color.WHITE);
    _pageText.setBounds(350,580,92,20);
    addChild(_pageText);
    
    // Set PageText text
    int pageNum = _slide.getPageNum();
    int pageCount = _slideShow.getSlideCount();
    _pageText.setText(pageNum + " of " + pageCount);
    
    // Create badge
    addBadgeImage(_slideShow._img);
    
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
public void addHeader()
{
    // Create/configure HeaderView
    _headerView = new TextArea();
    _headerView.setAlign(Pos.TOP_CENTER);
    _headerView.setPadding(16,16,16,16);
    _headerView.setWrapText(true);
    _headerView.setFont(HEADER_FONT);
    _headerView.setTextFill(Color.WHITE);
    _headerView.setEffect(SHADOW_EFFECT);
    
    // Set bounds and add
    _headerView.setBounds(36,18,720,130);
    addChild(_headerView);
    
    // If no items size to page, Align center (vertically)
    if(_slide.isTitleSlide()) {
        _headerView.setHeight(_headerView.getHeight()+250);
        _headerView.setFont(HEADER_FONT_BIG);
        _headerView.setAlign(Pos.CENTER);
        _headerView.setEffect(SHADOW_EFFECT_MORE);
        _bodyView.setY(_bodyView.getY()+180);
        _bodyView.setHeight(_bodyView.getHeight()-180);
        _bodyView.setAlign(Pos.TOP_CENTER);
    }
    
    // Set text and scale-to-fit if needed
    String text = _slide.getText();
    _headerView.setText(text);
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
    if(_slide.isTitleSlide()) text = text.trim();
    else if(text.startsWith("\t\t")) text = text.replace("\t\t", "\t\u2022 ");
    else if(text.startsWith("\t")) text = text.replace("\t", "\u2022 ");
    
    // Create TextArea
    TextArea textView = new TextArea(); textView.setGrowWidth(true);
    textView.setText(text); textView.setEditable(false); textView.setWrapText(true);
    textView.setPickable(false);
    textView.setFill(null); textView.setTextFill(Color.WHITE);
    textView.setFont(getItemFont());
    textView.addEventHandler(e -> textView.setFill(_color), MouseEnter);
    textView.addEventHandler(e -> textView.setFill(null), MouseExit);
    
    if(_slide.isTitleSlide())
        textView.setAlign(Pos.CENTER);
    else {
        textView.setDefaultLineStyle(textView.getDefaultLineStyle().copyFor(TextLineStyle.LEFT_INDENT_KEY, 40));
    }
    
    // Add to body view
    _bodyView.addChild(textView);
}

/**
 * Return Item font based on length.
 */
private Font getItemFont()
{
    int mlen = _slide.getMaxItemLength();
    if(mlen<20) return ITEM_FONT_HUGE;
    if(mlen<40) return ITEM_FONT_BIG;
    return ITEM_FONT;
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

/**
 * Adds a badge image to page.
 */
protected void addBadgeImage(Image anImage)
{
    // Create ImageView
    ImageView iview = new ImageView(anImage);
    iview.setMargin(10,10,10,10);
    iview.setManaged(false);
    iview.setLean(Pos.BOTTOM_RIGHT);
    iview.setEffect(new ShadowEffect(6,Color.BLACK,1,1)); iview.setFill(Color.CLEAR);
    
    // Size ImageView
    if(anImage.isLoaded()) iview.setSize(iview.getPrefSize());
    else anImage.addLoadListener(() -> iview.setSize(iview.getPrefSize()));
    
    // Add to this SlideView
    addChild(iview);
}

/**
 * This method resizes slide items to make sure they fit.
 */
protected void shrinkItemFontsToFit()
{
    // While body view wants to grow off page, shrink item text fonts
    double scale = 1;
    while(_bodyView.getPrefHeight(_bodyView.getWidth())>_bodyView.getHeight()) {
        
        // Reduce scale
        scale -= .05;
        
        // Iterate over children and set new scale
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
        
        // Relayout body
        _bodyView.relayout();
    }
}

/**
 * Handle events.
 */
protected void processEvent(ViewEvent anEvent)
{
    // Handle mouse click
    if(anEvent.isMouseRelease()) {
        if(anEvent.getX()>getWidth()/3) getPlayer().nextSlide();
        else getPlayer().prevSlide();
    }
}

}