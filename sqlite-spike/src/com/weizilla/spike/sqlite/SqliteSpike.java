package com.weizilla.spike.sqlite;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class SqliteSpike extends Activity
{
    private StringBuilder messageBuilder;
    private TextView msgText;
    private EditText nameInput;
    private EditText sizeInput;
    private EditText colorInput;
    private FruitStorage fruitStorage;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        msgText = (TextView) this.findViewById(R.id.message);
        msgText.setMovementMethod(new ScrollingMovementMethod());

        nameInput = (EditText) this.findViewById(R.id.name);
        sizeInput = (EditText) this.findViewById(R.id.size);
        colorInput = (EditText) this.findViewById(R.id.color);

        fruitStorage = new FruitStorage(this);
        messageBuilder = new StringBuilder();
        updateMessage("Started");
    }

    public void create(View view)
    {
        Fruit fruit = new Fruit(getNameInput(), getSizeInput());
        long id = fruitStorage.addFruit(fruit);
        updateMessage("CREATED: " + id);
    }

    public void update(View view)
    {
        String fruitName = getNameInput();
        if (fruitName.isEmpty())
        {
            updateMessage("Cannot update fruit with empty name");
            return;
        }

        Fruit fruit = new Fruit(getNameInput(), getSizeInput());
        int numUpdated = fruitStorage.updateFruit(fruit);
        updateMessage("Updated num of fruits: " + numUpdated);
    }

    public void read(View view)
    {
        String fruitName = getNameInput();
        if (fruitName.isEmpty())
        {
            updateMessage("Cannot search for empty fruit name");
            return;
        }

        Fruit fruit = fruitStorage.findFruit(fruitName);
        if (fruit == null)
        {
            updateMessage("No fruit found with name " + fruitName);
        }
        else
        {
            updateMessage("Fruit found: " + fruit);
        }
    }

    public void delete(View view)
    {
        String fruitName = getNameInput();
        int numDeleted = fruitStorage.deleteFruit(fruitName);
        updateMessage("Deleted num of fruits: " + numDeleted);
    }

    public void updateMessage(String message)
    {
        msgText = (TextView) this.findViewById(R.id.message);
        String newLine = new Date().toString() + " - " + message + "\n";
        messageBuilder.insert(0, newLine);
        msgText.setText(messageBuilder.toString());
    }

    public void clear(View view)
    {
        messageBuilder = new StringBuilder();
        msgText.setText("");
    }

    public String getNameInput()
    {
        return nameInput.getText().toString().trim();
    }

    public String getSizeInput()
    {
        return sizeInput.getText().toString().trim();
    }

    public String getColorInput()
    {
        return colorInput.getText().toString().trim();
    }
}
