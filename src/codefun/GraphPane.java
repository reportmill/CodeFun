package codefun;
import snap.gfx.*;
import snap.view.*;

/**
 * Provides UI for GraphView.
 */
public class GraphPane extends ViewOwner {
    
    // The GraphView
    GraphView     _gview = new GraphView();
    
    // The function text
    TextField     _text = new TextField();

/** Create UI. */
protected View createUI()
{
    // Configure Text
    _text.setName("Text"); _text.setColCount(30);
    
    // Configure buttons
    Button btn = new Button("Graph"); btn.setName("GraphButton"); btn.setPrefSize(70,22);
    Button lbtn = new Button("Linear"); lbtn.setName("Linear"); lbtn.setPrefSize(60,22); lbtn.setLeanX(HPos.RIGHT);
    Button qbtn = new Button("Quad"); qbtn.setName("Quad"); qbtn.setPrefSize(60,22);
    Button cbtn = new Button("Cubic"); cbtn.setName("Cubic"); cbtn.setPrefSize(60,22);
    
    // Configure HBox for label and text
    RowView hbox = new RowView(); hbox.setPadding(10,10,10,10); hbox.setSpacing(10); hbox.setGrowWidth(true);
    hbox.setChildren(new Label("Enter Function:"), _text, btn, lbtn, qbtn, cbtn);

    // Configure VBox for HBox and GraphView
    ColView vbox = new ColView(); vbox.setChildren(hbox, _gview); vbox.setPadding(10,10,10,10);
    return vbox;
}

/** Initialize UI. */
protected void initUI()
{
    _gview.setFunction(GraphView.CUBIC_EQ);
}

/** Reset UI. */
protected void resetUI()
{
    setViewText("Text", _gview.getFunction());
}

/** Respond UI. */
protected void respondUI(ViewEvent anEvent)
{
    if(anEvent.equals("Text") || anEvent.equals("GraphButton"))
        _gview.setFunction(getViewText("Text"));
    if(anEvent.equals("Linear"))
        _gview.setFunction(GraphView.LINEAR_EQ);
    if(anEvent.equals("Quad"))
        _gview.setFunction(GraphView.QUAD_EQ);
    if(anEvent.equals("Cubic"))
        _gview.setFunction(GraphView.CUBIC_EQ);
}

public static void main(String args[])
{
    GraphPane gpane = new GraphPane();
    gpane.getUI().setGrowWidth(true);
    gpane.getUI().setFill(new Color("#FFEEFF"));
    gpane.getUI().setBorder(Color.LIGHTGRAY,1);
    gpane.setWindowVisible(true);
}

}