package LOSearch;

import java.util.ArrayList;

import edu.udo.cs.wvtool.generic.output.WVTOutputFilter;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.util.WVToolException;


public class WordVectorMemoryWriter implements WVTOutputFilter {

	private ArrayList<WVTWordVector> list = new ArrayList<WVTWordVector>();
	public void write(WVTWordVector arg0) throws WVToolException {
		arg0.getValues();
		WVTWordVector vec = new WVTWordVector();
		for ( double d : arg0.getValues() )
			System.out.print(d+" ");
		System.out.println();
		vec.setValues(arg0.getValues());
		vec.setDocumentInfo(new WVTDocumentInfo(arg0.getDocumentInfo().getSourceName(), arg0.getDocumentInfo().getContentType(), "" , "en" , arg0.getDocumentInfo().getClassValue()));
		list.add(vec);
	}
	public ArrayList<WVTWordVector> getList() {
		return list;
	}

}
