package com.ciotc.teemo.file.frame;

import static com.ciotc.teemo.util.SwingConsole.getSizeDependOnScreen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.ciotc.teemo.file.template.DisplayFileTemplate;
import com.ciotc.teemo.file.view.Display2DView;
import com.ciotc.teemo.file.view.DisplayView;
/**
 * 使JInternalFrame更美观<hr>a</hr>
 * 摘自<a href=http://devbean.blog.51cto.com/448512/92426>FinderCheng 的BLOG</a>
 * 从2.0开始 有工具栏
 * @author FinderCheng(部分),林晓智
 * @version 2.0
 */
public class DisplayInternalFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The is hidden. */
	boolean isHidden = false;

	/** The old ui. */
	BasicInternalFrameUI oldUi = null;

	int preWidth ;
	int preHeight ;

	private DisplayView view = null;
	/**
	 * 状态栏
	 */
	//private JTextField statusField = null;
	
	/**
	 * 一次缩放的像素值
	 */
	int rotateUnitWidth = 52; 
	int rotateUnitHeight = 44;
	int minWidthHeight = 0;
	
	int h0 = 109; //原来是103
	int w0 =35 ;//原来是36
	/**
	 * 
	 */
	int rotateNum = 0;
	
	int maxWidth = 0; //最大的宽度
	int maxHeight = 0;
	
	
	DisplayFileTemplate displayFileTemplate ;
	
	public DisplayInternalFrame(DisplayFileTemplate displayFileTemplate,String title){
		this(title);
		this.displayFileTemplate = displayFileTemplate;
		
	}
	
	/**
	 * Instantiates a new inner frame.
	 */
	public DisplayInternalFrame(String title) {
		super(title, true, true, false, true);
		oldUi = (BasicInternalFrameUI) getUI();
		//setSize(528, 496);//468=460+8 456=400+56 not 8 and 56 is 30 and 108
		int constantWidth = rotateUnitWidth * 10 + w0;
		int constantHeight = rotateUnitHeight * 10 + h0;
		setSize(constantWidth,constantHeight);

		setVisible(false); //先置假
	
		setMinimumSize(new Dimension(constantWidth,constantHeight)); //468 456
		setPreferredSize(new Dimension(constantWidth,constantHeight));
		//设置图的最大尺寸
		Dimension dimension = getSizeDependOnScreen(0.8f,0.8f);
		maxWidth = dimension.width ;
		maxHeight = dimension.height;
		//System.out.println("MaxSize:"+maxWidth+","+maxHeight);
 
		if(maxWidth < maxHeight){
			minWidthHeight = 0;
			rotateNum = (maxWidth - constantWidth ) / rotateUnitWidth ;
			int remainder = (maxWidth - constantWidth ) % rotateUnitWidth;
			if(remainder <= 26){ //22 == 44 /2
				maxWidth -= remainder;
				maxHeight = constantHeight + rotateNum * rotateUnitHeight;
			}
			else{
				maxWidth += (rotateUnitWidth-remainder);
				rotateNum += 1;
				maxHeight = constantHeight + rotateNum * rotateUnitHeight;
			}
		}else{
			minWidthHeight = 1;
			rotateNum = (maxHeight - constantHeight ) / rotateUnitHeight ;
			int remainder = (maxHeight - constantHeight ) % rotateUnitHeight;
			if(remainder <= 22){ // 19 == 40 /2 -1
				maxHeight -= remainder;
				maxWidth = constantWidth + rotateNum * rotateUnitWidth;
			}
			else{
				maxHeight += (rotateUnitHeight-remainder);
				rotateNum += 1;
				maxWidth = constantWidth + rotateNum * rotateUnitWidth;
			}
		}	
		setMaximumSize(new Dimension(maxWidth,maxHeight));
		
		//System.out.println("MaxSize:"+maxWidth+","+maxHeight);
		addInternalFrameListener(new MyInterFrameAdapter());
		addComponentListener(new MyComponentAdapter());
		
		setLayout(new BorderLayout());
		//addMouseWheelListener(new MyMouseWheelListener());
		
		
		setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		pack();
		//this.getContentPane().add(statusField,BorderLayout.SOUTH);
		//System.out.println(getDefaultCloseOperation());
				
	}

	
	public void addDisplayView(DisplayView view){
		this.view = view;
		//this.getContentPane().add(this.view,BorderLayout.CENTER);
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.add(new JPanel(),BorderLayout.EAST);
		jp.add(new JPanel(),BorderLayout.WEST);
		jp.add(new JPanel(),BorderLayout.NORTH);
		jp.add(new JPanel(),BorderLayout.SOUTH);
		jp.add(view);
		add(jp);
		
		//add(view,BorderLayout.CENTER);
	}
	
	
	
	public DisplayView getView() {
		return view;
	}


	

//	@Override
//	public Dimension getPreferredSize() {
//		return new Dimension(550, 548);
//	}
//
//	@Override
//	public Dimension getMinimumSize() {
//		return new Dimension(550, 548);
//	}




	class MyComponentAdapter extends ComponentAdapter{
//		@Override
//		public void componentHidden(ComponentEvent e) {
//			super.componentHidden(e);
//			DisplayFileTemplate fileTemplate = getDisplayFileTemplate();
//			super.componentHidden(e);
//			if(fileTemplate != null){
//				fileTemplate.setClose(DisplayInternalFrame.this);
//			}
//			if(!fileTemplate.isExist2D && !fileTemplate.isExist3D && !fileTemplate.isExistGrpah);
//			//	fileTemplate.desktopPane.removeFileTemplate(fileTemplate);
//		}
		
		public void componentResized(ComponentEvent e) {
			
			DisplayInternalFrame selectedFrame = (DisplayInternalFrame) e.getSource();
			Dimension rect = selectedFrame.getSize();
			//DisplayView view = selectedFrame.getView();
			int nWidth ,nHeight;

			nWidth=  rect.width;
			nHeight = rect.height;
			if(nWidth == selectedFrame.preWidth && nHeight == selectedFrame.preHeight)
				return;

			if(nWidth != selectedFrame.preWidth){    // 宽度改变  
				//注释掉的只能整数变像素
//				int i = (nWidth - w0) / DisplayView.colNum;
//				int remainder =  (nWidth - w0) % DisplayView.colNum;
//				if(remainder <=26){
//					rect.width -= remainder;
//					rect.height = i * DisplayView.rowNum + h0;
//				}else{
//					rect.width += (rotateUnitWidth-remainder);
//					i++;
//					rect.height = i * DisplayView.rowNum + h0;
//				}
				
				float i = Math.round((nWidth - w0) * 1.0f / DisplayView.colNum * 10) * 1.0f / 10;
				rect.width = Math.round(i * DisplayView.colNum + w0);
				rect.height = Math.round(i * DisplayView.rowNum + h0);
				
				//rect.height =  Math.round((nWidth - w0) * 1.0f * DisplayView.rowNum / DisplayView.colNum + h0); 
				
				
			}else if(nHeight != selectedFrame.preHeight) {   // 高度改变  			
				
//				int i = (nHeight - h0) / DisplayView.rowNum;
//				int remainder =  (nHeight - h0) % DisplayView.rowNum;
//				if(remainder <=22){
//					rect.height -= remainder;
//					rect.width = i * DisplayView.colNum + w0;
//				}else{
//					rect.height += (rotateUnitHeight-remainder);
//					i++;
//					rect.width = i * DisplayView.colNum + w0;
//				}
				float i = Math.round((nHeight - h0) * 1.0f / DisplayView.rowNum *10)* 1.0f / 10;
				rect.width = Math.round(i * DisplayView.colNum + w0);
				rect.height = Math.round(i * DisplayView.rowNum + h0);
				
				//rect.width = Math.round((nHeight - h0) * 1.0f * DisplayView.colNum / DisplayView.rowNum + w0);
			}  
			
			setSize(rect.width,rect.height);
			selectedFrame.preWidth = rect.width;
			selectedFrame.preHeight = rect.height;
			
		}
		
		@Override
		public void componentShown(ComponentEvent e) {
			displayFileTemplate.setOpen(DisplayInternalFrame.this);
		}

		@Override
		public void componentHidden(ComponentEvent e) {	
			displayFileTemplate.setClose(DisplayInternalFrame.this);
		}
	}
	
	
	class MyNewComponentAdapter extends ComponentAdapter{
//		public void componentResized(ComponentEvent e) {
//			DisplayInternalFrame selectedFrame = (DisplayInternalFrame) e.getSource();
//			Dimension rect = selectedFrame.getSize();
//			if(minWidthHeight == 0)
//				view.onSize(rect.width, 0);
//			else if(minWidthHeight == 1)
//				view.onSize(rect.height, 1);
//		}
		
		
		
	}
	
	class MyInterFrameAdapter extends InternalFrameAdapter{
				
		@Override
		public void internalFrameClosed(InternalFrameEvent e) {
			//displayFileTemplate.setClose(DisplayInternalFrame.this);
		}

		@Override
		public void internalFrameClosing(InternalFrameEvent e) {
			
			//super.internalFrameClosing(e);
			//System.out.println("closing");
			//DisplayInternalFrame.this.getView().
			if(view instanceof Display2DView){
//				//如果控制点没有做过修改 就不保存
				displayFileTemplate.save();
////				if(!displayFileTemplate.archPoints.isDirty() /*&& !displayFileTemplate.ddoc.isDirty()*/) return;
////				//保存控制点
////				int answer = JOptionPane.showConfirmDialog(new JFrame(),
////						getResourceString("isSaveComment"), getResourceString("pleaseConfirm"), JOptionPane.YES_NO_OPTION);
////				if(answer== JOptionPane.YES_OPTION){
////					displayFileTemplate.archPoints.savePoints();
////					if(displayFileTemplate.ddoc.saveArchPoints2File()){
////						JOptionPane.showMessageDialog(DisplayInternalFrame.this,getResourceString("saveSuccess"),getResourceString("Teemo"),JOptionPane.INFORMATION_MESSAGE);
////					}else{
////						JOptionPane.showMessageDialog(DisplayInternalFrame.this,getResourceString("saveFail"),getResourceString("Teemo"),JOptionPane.INFORMATION_MESSAGE);
////					}
////				}
			}
		}

//		@Override
//		public void internalFrameClosed(InternalFrameEvent e) {
//			//super.internalFrameClosed(e);
//			//System.out.println("close");
//			switch(DisplayInternalFrame.this.getDefaultCloseOperation()){
//			case DO_NOTHING_ON_CLOSE:
//				System.out.println("close");
//				//DisplayInternalFrame.this.view.close();
//				System.exit(0);
//				break;
//			default:
//				break;
//			}
//			
//		}
//
		
	}
	
	
	
	
//	class MyMouseWheelListener implements MouseWheelListener{
//
//		@Override
//		public void mouseWheelMoved(MouseWheelEvent e) {
//			// TODO Auto-generated method stub
//			int rotate = e.getWheelRotation();
//			//System.out.println("wheelroate:"+rotate);
//			
//			Dimension rect = getSize();
//			//System.out.println("size:"+rect.width+","+rect.height);
//			//System.out.println(minWidthHeight);
//			if(minWidthHeight == 0){
//				if(rotate >0 && rect.width >= maxWidth) return;
//				if(rotate < 0 && rect.width <= 528/*minWidth*/) return;
//			}else if(minWidthHeight == 1){
//				if(rotate >0 && rect.height >= maxHeight) return;
//				if(rotate < 0 && rect.height <= 496) return;
//			}
//			setSize(rect.width+rotate*rotateUnitWidth, rect.height+rotate*rotateUnitHeight);
//		}
//		
//	}




	public DisplayFileTemplate getDisplayFileTemplate() {
		return displayFileTemplate;
	}
	
	
	
	
}




