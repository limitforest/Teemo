package test;

import java.io.File;

import org.dcm4che2.tool.dcmsnd.DcmSnd;
import org.dcm4che2.tool.jpg2dcm.Jpg2Dcm;
import org.dcm4che2.util.StringUtils;
import org.dcm4che2.util.UIDUtils;

import com.ciotc.teemo.util.Constant;
import com.ciotc.teemo.util.GeneralUtils;

public class Test {

	public static void main( String[] args ) throws Exception {

		Jpg2Dcm j2d = new Jpg2Dcm( );
		j2d.setCharset( "GBK" );
		String stydyuid = UIDUtils.createUID( );
		String seriesuid = UIDUtils.createUID( );
		for ( int i = 1; i <= 10; i++ ) {

			j2d.convert( new File( "C:\\Users\\lenovo\\Desktop\\dicom\\2Dview" + i + ".jpg" ), new File(
					"C:\\Users\\lenovo\\Desktop\\dicom\\2Dview" + i + ".dcm" ), "康威", "000003", stydyuid, seriesuid, i );
		}

		String aet = GeneralUtils.getProp( ).getString( Constant.CONFIG_KEY_PACS_AET );
		String host = GeneralUtils.getProp( ).getString( Constant.CONFIG_KEY_PACS_HOST );
		int port = GeneralUtils.getProp( ).getInt( Constant.CONFIG_KEY_PACS_PORT );

		// "DCM4CHEE@192.168.110.56:11112"
		StringBuffer commandsb = new StringBuffer( aet + "@" + host + ":" + port );
		for ( int i = 1; i <= 10; i++ ) {
			commandsb.append( " " );
			commandsb.append( "C:\\Users\\lenovo\\Desktop\\dicom\\2Dview" + i + ".dcm" );
		}
		String[] commands = StringUtils.split( commandsb.toString( ), ' ' );
		DcmSnd.execute( commands );

	}

}
