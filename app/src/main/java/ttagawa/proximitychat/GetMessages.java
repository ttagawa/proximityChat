
package ttagawa.proximitychat;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMessages {

    @SerializedName("result_list")
    @Expose
    public List<ResultList> resultList = new ArrayList<ResultList>();
    @SerializedName("result")
    @Expose
    public String result;

}
