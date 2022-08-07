package bool;

import org.json.JSONObject;


public class NodeFactory {
    String jsonString = "
    {
        'node': 
        'and', 
        'subnode1' : {
            'node': 
            'or', 
            'subnode1': {
                'node': 'value', 'value': true
            },
            'subnode2': {
                'node': 'value', 'value': false
            }
        },
        'subnode2': {
            'node': 'value', 'value': true
        }
    }"
    
    JSONObject jsonObject = (JSONObject) JSONParser.parseString(jsonString);
    public BooleanNode getBooleanNode(JSONObject input) {
        if(input == null){
            return null;
         }		
         System.out.println(input.toString());
         
         return null;
    }
}