import org.json4s.JsonAST.JNothing
import org.json4s.jackson.JsonMethods._

val json = parse("""
    {
    	"blah": [{
    			"first": "blah1",
    			"second": "blah11"
    		}, {
    			"first": "blah2"
    		}, {
    			"first": "blah3",
    			"second": "blah33"
    		}
    	]
    }
""")

(json \ "blah").children.collect {
  case j if j \ "second" != JNothing => j
}
