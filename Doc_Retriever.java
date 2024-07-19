package DocumentRetriever;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
public class UI implements ActionListener{
    JLabel label,label2;
    JFrame frame;
    JTextField textfield;
    JButton searchbutton;
    JButton openbutton;
    JComboBox<String> comboBox;
    GridBagConstraints gbc=new GridBagConstraints();
    private static final String API_KEY = "<YOUR_API_KEY>";
    private static final String SEARCH_ENGINE_ID = "<SEARCH_ENGINE_ID>";

    UI(){
    frame=new JFrame("Document Retireiver");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400,150);
    frame.setLayout(new GridBagLayout());

    label=new JLabel("Enter text: ");
    textfield=new JTextField(20);
    
    searchbutton=new JButton("Search");
    searchbutton.addActionListener(this);
    
    gbc.insets=new Insets(5,5,5,5);
    
    gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        frame.add(textfield, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        frame.add(searchbutton, gbc);

        frame.setVisible(true);
        
       
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==searchbutton){
            if (label2 != null) {
                frame.remove(label2);
            }
            if (comboBox != null) {
                frame.remove(comboBox);
            }
            if (openbutton != null) {
                frame.remove(openbutton);
            }
        String key=textfield.getText();
        String[] options=getPDFurls(key);
        label2=new JLabel("Select Document: ");
        openbutton=new JButton("Open");
        openbutton.addActionListener(this);
        comboBox = new JComboBox<>(options);
        comboBox.addActionListener(this);
        //GridBagConstraints gbc=new GridBagConstraints();
        //gbc.insets=new Insets(5,5,5,5);
        gbc.gridx=0;
        gbc.gridy=1;
        frame.add(label2,gbc);
        gbc.gridx=1;
        gbc.gridy=1;
        frame.add(comboBox,gbc);
        gbc.gridx=2;
        gbc.gridy=1;
    
        frame.add(openbutton,gbc);
        frame.pack();
        
        frame.setVisible(true);
        }
        if(e.getSource()==openbutton){
            try{
                Desktop.getDesktop().browse(new java.net.URI((String) comboBox.getSelectedItem()));
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }


        // JTextField tf=new JTextField(20);     
        // tf.setText(key);
        
        // gbc.gridx = 1; // Position in the new row
        // gbc.gridy = GridBagConstraints.RELATIVE; // Increment row automatically
        // frame.add(tf,gbc);
        // frame.pack();
    }
    private String[] getPDFurls(String key){
        String searchUrl = "https://www.googleapis.com/customsearch/v1?q=" + key + "+filetype:pdf&key=" + API_KEY + "&cx=" + SEARCH_ENGINE_ID;
        try{
            HttpClient httpClient = HttpClients.createDefault();

            // Create HttpGet request
            HttpGet httpGet = new HttpGet(searchUrl);

            // Execute request and get response
            String response = httpClient.execute(httpGet, httpResponse -> {
                // Ensure the response entity is consumable
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                return responseBody;
            });
            JSONObject json=new JSONObject(response);
            JSONArray items = json.getJSONArray("items");
            String[] urls = new String[items.length()];
            for (int i = 0; i < items.length(); i++) {
                urls[i] = items.getJSONObject(i).getString("link");
            }
            return urls;
        } catch (IOException ex) {
            ex.printStackTrace();
            return new String[]{};
        }
        }
    
    public static void main(String args[]){
        new UI();
    }
}

