package codefun;
import snap.view.*;

/**
 * A custom class.
 */
public class Calc extends ViewOwner {
    
    // The main and calculated values
    int   _num, _alt;
    
    // The last operation
    char   _op;

    
/**
 * ResetUI.
 */
public void resetUI()
{
    setViewValue("Text", _num);
    setViewValue("AltText", _alt);
}

/**
 * RespondUI.
 */
public void respondUI(ViewEvent anEvent)
{
    // if last op was equals, clear
    if(_op=='=' && anEvent.getName().startsWith("Button"))
        _num = _alt = _op = 0;
        
    if(anEvent.equals("Button0"))
        _num = _num*10;
    if(anEvent.equals("Button1"))
        _num = _num*10 + 1;
    if(anEvent.equals("Button2"))
        _num = _num*10 + 2;
    if(anEvent.equals("Button3"))
        _num = _num*10 + 3;
    if(anEvent.equals("Button4"))
        _num = _num*10 + 4;
    if(anEvent.equals("Button5"))
        _num = _num*10 + 5;
    if(anEvent.equals("Button6"))
        _num = _num*10 + 6;
    if(anEvent.equals("Button7"))
        _num = _num*10 + 7;
    if(anEvent.equals("Button8"))
        _num = _num*10 + 8;
    if(anEvent.equals("Button9"))
        _num = _num*10 + 9;
        
    if(anEvent.equals("Plus"))
        doCalc('+');
    if(anEvent.equals("Minus"))
        doCalc('-');
    if(anEvent.equals("Multiply"))
        doCalc('*');
    if(anEvent.equals("Divide"))
        doCalc('/');
    if(anEvent.equals("Equals"))
        doCalc('=');
    if(anEvent.equals("Clear"))
        _num = _alt = _op = 0;
}


/**
 * Do calculation.
 */
public void doCalc(char anOp)
{
    switch(_op) {
        case '+': _alt = _alt + _num; break;
        case '-': _alt = _alt - _num; break;
        case '*': _alt = _alt * _num; break;
        case '/': _alt = _alt / _num; break;
        default: _alt = _num;
    }
    
    _num = 0;
    _op = anOp;
    
    if(anOp=='=')
        _num = _alt;
}

/**
 * Standard main method.
 */
public static void main(String args[])
{
    //snaptea.TV.set();
    new Calc().setWindowVisible(true);
}

}