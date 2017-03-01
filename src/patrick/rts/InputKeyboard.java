package patrick.rts;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputKeyboard implements KeyListener
{
	private enum KeyState {
	    RELEASED, // Not down
	    PRESSED,  // Down, but not the first time
	    ONCE      // Down for the first time
	  }
	private static final int KEY_COUNT = 256;
	        
	  // Current state of the keyboard
	  private boolean[] currentKeys = null;
	        
	  // Polled keyboard state
	  private KeyState[] keys = null;
	        
	  public InputKeyboard() {
	    currentKeys = new boolean[ KEY_COUNT ];
	    keys = new KeyState[ KEY_COUNT ];
	    for( int i = 0; i < KEY_COUNT; ++i ) {
	      keys[ i ] = KeyState.RELEASED;
	    }
	  }
	        
	  public synchronized void poll() {
	    for( int i = 0; i < KEY_COUNT; ++i ) {
	      // Set the key state 
	      if( currentKeys[ i ] ) {
	        // If the key is down now, but was not
	        // down last frame, set it to ONCE,
	        // otherwise, set it to PRESSED
	        if( keys[ i ] == KeyState.RELEASED )
	          keys[ i ] = KeyState.ONCE;
	        else
	          keys[ i ] = KeyState.PRESSED;
	      } else {
	        keys[ i ] = KeyState.RELEASED;
	      }
	    }
	  }
	        
	  public boolean keyDown( int keyCode ) {
	    return keys[ keyCode ] == KeyState.ONCE ||
	           keys[ keyCode ] == KeyState.PRESSED;
	  }
	        
	  public boolean keyDownOnce( int keyCode ) {
	    return keys[ keyCode ] == KeyState.ONCE;
	  }
	        
	  public synchronized void keyPressed( KeyEvent e ) {
	    int keyCode = e.getKeyCode();
	    if( keyCode >= 0 && keyCode < KEY_COUNT ) {
	      currentKeys[ keyCode ] = true;
	    }
	  }

	  public synchronized void keyReleased( KeyEvent e ) {
	    int keyCode = e.getKeyCode();
	    if( keyCode >= 0 && keyCode < KEY_COUNT ) {
	      currentKeys[ keyCode ] = false;
	    }
	  }

	  public void keyTyped( KeyEvent e ) {
	    // Not needed
	  }
}
