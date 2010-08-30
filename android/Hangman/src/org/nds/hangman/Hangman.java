package org.nds.hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Hangman extends Activity {

	private static final String TAG = "Hangman Activity";

	private static final String CHAR_NOT_FOUND = "_";

	private static final String DIC_FOLDER = "dictionaries";
	private static final String DIC_FILE_EXT = "dico";
	
	private static final int DLG_LEVEL = 0;
	
	private enum Level {
		EASY(0, "easy"),
		MIDDLE(1, "middle"),
		DIFFICULT(2, "difficult"),
		TREMULOUS(3, "tremulous");
		
		private int levelNumber;
		private String name;
		
		private Level(int levelNumber, String name) {
			this.levelNumber = levelNumber;
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public int getLevelNumber() {
			return levelNumber;
		}
		
		public static Level getLevel(int levelNumber) {
			for(Level level : values()) {
				if(levelNumber == level.getLevelNumber()) {
					return level;
				}
			}
			
			return Level.EASY;
		}
	}
	
	private String secretWord;
	private Level level = Level.EASY;
	private int errors;

	private List<Button> buttonsDisabled = new ArrayList<Button>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// View layout = (View)findViewById(R.id.LayoutMain);
		// 
		// BitmapDrawable MyTiledBG = new
		// BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.back));
		// MyTiledBG.setTileModeXY(Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
		// layout.setBackgroundDrawable(MyTiledBG);
		
		ContentValues values = new ContentValues();
		values.put("field1", "value1");
		
		managedQuery(Uri.parse("content://org.nds.database.hangmanprovider"), null, null, null, null);
		getContentResolver().insert(Uri.parse("content://org.nds.database.hangmanprovider"), values);
		getContentResolver().update(Uri.parse("content://org.nds.database.hangmanprovider"), values, null, null);
		
		managedQuery(Uri.parse("content://org.nds.database.entityprovider"), null, null, null, null);
		getContentResolver().insert(Uri.parse("content://org.nds.database.entityprovider"), values);
		getContentResolver().update(Uri.parse("content://org.nds.database.entityprovider"), values, null, null);
		
		newWord(null);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
    		case DLG_LEVEL:
	    		//CharSequence[] items = getResources().getTextArray(R.array.level);
	    		
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	       		builder.setTitle("Faite votre choix");
	       		builder.setSingleChoiceItems(R.array.level, level.getLevelNumber(), new DialogInterface.OnClickListener() {
	       		    public void onClick(DialogInterface dialog, int item) {
	       		        level = Level.getLevel(item);
	       		        Log.d(TAG + "[Level selected]", "level item selected: " + level.getName());
	       		    }
	       		});
	
	            dialog = builder.create();
	            break;
    	    default:
    	        dialog = null;
    	}
    	
    	return dialog;

	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.d(TAG + "[onConfigurationChanged]", newConfig.toString());
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        Log.d(TAG, "Menu created.");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemNew: {
                newWord(null);
                return true;
            }
            case R.id.itemExit: {
                exit(null);
                return true;
            }
            case R.id.itemLevel: {
            	showDialog(DLG_LEVEL);
                return true;
            }
        }
        return false;
    }
    
	public void exit(View target) {
		this.finish();
	}
	
	public void newWord(View target) {
		String locale = getResources().getConfiguration().locale.getLanguage();
		String randomWord = loadRandomWord(locale, level.getName());
		
		secretWord = randomWord;
		// Reset the number of errors
		errors = 0;

		// Initialize the secret word view
		LinearLayout secretWordLayout = (LinearLayout) findViewById(R.id.SecretWord);
		secretWordLayout.removeAllViews();
		for (int i = 0; i < secretWord.length(); i++) {
			TextView charSWView = new TextView(this);
			charSWView.setTextSize(24);
			charSWView.setText(CHAR_NOT_FOUND);
			if (i != 0) {
				charSWView.setPadding(5, 0, 0, 0);
			}
			secretWordLayout.addView(charSWView);
		}

		// Reset image
		ImageView img = (ImageView) findViewById(R.id.ImageViewHangman);
		img.setImageResource(R.drawable.hang0);

		// Display keyboard layout
		View keyboardLayout = findViewById(R.id.KeyboardLayout);
		keyboardLayout.setVisibility(View.VISIBLE);
		// hide result layout
		View LayoutResult = findViewById(R.id.LayoutResult);
		LayoutResult.setVisibility(View.GONE);

		// Reset buttons
		for (Button b : buttonsDisabled) {
			b.setEnabled(true);
			b.setVisibility(View.VISIBLE);
		}
		buttonsDisabled.clear();

		// Reset letter pressed view
		TextView textView = (TextView) findViewById(R.id.LettersPressed);
		textView.setText("");
	}

	private String loadRandomWord(String locale, String level) {
		String randomWord;
		
		List<String> file = new ArrayList<String>();
		String line;
		InputStream is = null;
		BufferedReader buf = null;
		try {
			String filePath = DIC_FOLDER + "/"  + (locale != null ? locale + "/" : "") + level + "." + DIC_FILE_EXT;
			Log.d(TAG + "[LoadRandomWord]", "file path: " + filePath);
			is = getResources().getAssets().open(filePath);
			buf = new BufferedReader(new InputStreamReader(is));
			if (!buf.ready()) {
				throw new IOException();
			}
			while ((line = buf.readLine()) != null) {
				file.add(line);
			}
			Log.d(TAG + "[LoadRandomWord]", "lines: " + file);
			// Getting random int in the range of the element of arraylist
			int randomInt = (int)(Math.random() * file.size());
			
			// Getting random message
			randomWord = (String)file.get(randomInt);
		} catch (IOException e) {
			if(locale!=null) { // Avoid stack over flow
				Log.i(TAG + "[LoadRandomWord]", "Dictionary not found for the current locale " + locale +". Load the default dictionary.");
				randomWord = loadRandomWord(null, level);
			} else {
				Log.i(TAG + "[LoadRandomWord]", "Impossible to load a dictionary file -> no word!");
				randomWord = "";
			}
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
			try {
				buf.close();
			} catch (Exception e) {
			}
		}
		
		return randomWord.toUpperCase();
	}

	public void clickLetter(View target) {
		// Disable and hide button
		Button b = (Button) target;
		b.setEnabled(false);
		b.setVisibility(View.INVISIBLE);
		buttonsDisabled.add(b);
		Log.d(TAG + "[clickLetter]", "Click letter " + b.getText());

		// Update textViewSecretWord if the secret word contains the letter
		if (secretWord.contains(b.getText())) { // Good Letter
			boolean charNotfound = false;
			LinearLayout secretWordLayout = (LinearLayout) findViewById(R.id.SecretWord);
			for (int i = 0; i < secretWord.length(); i++) {
				TextView charSWView = (TextView) secretWordLayout.getChildAt(i);
				if (secretWord.charAt(i) == b.getText().charAt(0)) {
					charSWView.setText(b.getText());
				} else if (charSWView.getText().equals(CHAR_NOT_FOUND)) {
					charNotfound = true;
				}
			}
			// No more letter to found
			if (!charNotfound) { // Winner
				// Hide keyboard layout
				View keyboardLayout = findViewById(R.id.KeyboardLayout);
				keyboardLayout.setVisibility(View.GONE);
				// Display text result
				TextView textResult = (TextView) findViewById(R.id.TextViewResult);
				textResult.setText(R.string.win);
				textResult.setTextColor(getResources().getColor(R.color.green));
				View LayoutResult = findViewById(R.id.LayoutResult);
				LayoutResult.setVisibility(View.VISIBLE);
			}
		} else { // Wrong letter -> update hangman image
			errors++;
			Log.d(TAG + "[clickLetter]", "Number of errors: " + errors);
			int imgIdentifier = getResources().getIdentifier("hang" + errors, "drawable", getPackageName());
			ImageView img = (ImageView) findViewById(R.id.ImageViewHangman);
			img.setImageBitmap(BitmapFactory.decodeResource(getResources(),	imgIdentifier));
			if (errors == 10) { // Loser
				// Hide keyboard layout
				View keyboardLayout = findViewById(R.id.KeyboardLayout);
				keyboardLayout.setVisibility(View.GONE);
				// Display text result
				TextView textResult = (TextView) findViewById(R.id.TextViewResult);
				textResult.setText(R.string.lose);
				textResult.setTextColor(getResources().getColor(R.color.red));
				View LayoutResult = findViewById(R.id.LayoutResult);
				LayoutResult.setVisibility(View.VISIBLE);
				
				// Display missing letters
				LinearLayout secretWordLayout = (LinearLayout) findViewById(R.id.SecretWord);
				for (int i = 0; i < secretWord.length(); i++) {
					TextView charSWView = (TextView) secretWordLayout.getChildAt(i);
					if (charSWView.getText().equals(CHAR_NOT_FOUND)) {
						charSWView.setTextColor(getResources().getColor(R.color.red));
						charSWView.setText(String.valueOf(secretWord.charAt(i)));
					}
				}
			}
		}

		// Update the TextView with letters pressed
		TextView textView = (TextView) findViewById(R.id.LettersPressed);
		textView.setText(textView.getText() + " " + b.getText());
		Log.d(TAG + "[clickLetter]", "Letters already clicked: " + b.getText());
	}

}