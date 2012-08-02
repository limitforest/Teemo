package com.ciotc.teemo.file.view;

import static com.ciotc.teemo.resource.MyResources.getResourceString;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;


import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import com.ciotc.teemo.file.model.DisplayModel;
import com.ciotc.teemo.file.template.DisplayFileTemplate;
import com.ciotc.teemo.file.view.Display2DView.MyPopupListener;

public class Display3DView extends DisplayView {

	//绘图的常数变量
	int zoomx ;
	int zoomy;
	int shiftx ;
	int shifty ;	
	int dir;  //方向
	//缩放的比例
	float unit ; //10个像素
	float zoomh ;
	
	float lean;
	
	Point points[][] = new Point[45][53]; //51 51
	
	JPopupMenu popup; // 添加弹出注解的弹出式菜单
	/**
	 * 构造坐标
	 * @param data
	 */
	public void constructPoints(){
		int i ,j;

		for(i = 0;i<=rowNum;i++){
			for(j = 0;j<=colNum;j++){
				points[i][j] = new Point(j,i);
				//points[i][j].x = j;
				//points[i][j].y = i;

				points[i][j].x = (int) ((points[i][j].x* 1.0 + points[i][j].y * lean* 1.0) * zoomx * unit * 1.0);
		
				points[i][j].y *= zoomy * unit;
				points[i][j].x += shiftx;
				points[i][j].y += shifty;
				
			}
				
		}	
	}
	
	public void constructPopup() {
		popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem(getResourceString("comments"));
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//将评论内容的对话框显示出来
				dfTemplate.commentDialog.setVisible(true);
			}
		});
		popup.add(menuItem);
	}
	
	class MyPopupListener extends MouseAdapter {
		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		public void mousePressed(MouseEvent e) {
			// maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}
	}
	
	public Display3DView(DisplayFileTemplate dfTemplate) {
		super(dfTemplate);
		

		zoomx = 6;//缩放比例
		zoomy = 5;
	
		shiftx = 50;
		shifty = 200;  //300
	
		dir = 0;
	
		unit = 1; //10个像素
		zoomh = 0.7f; //1
	
			
		lean =0.5f; //0.5773;
	
				
		addMouseListener(new MyMouseAdapter());
		constructPoints();
		//添加弹出式菜单 和事件监听 
		//constructPopup();
		addMouseListener(new MyPopupListener());
	}
	

	@Override
	public void drawFrame(Graphics g, int[] dd) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
	//统一用宽度改变
		
		unit = (float) (getWidth()  * 0.1 / colNum);
		pixel = unit * 10;
		zoomh = (float) (getWidth() * 0.07 / colNum);
		shifty = (int) (getWidth() * 20.0 / colNum);
		if(dir == 0 || dir == 2){
			shiftx = (int) (getWidth() * 5.0 / colNum);
		}else 	if(dir == 1 || dir == 3){
			shiftx = (int) (getWidth() * 15.0 / colNum); 
		}
//		}else if(flag ==1){
//			unit = (float) ((lenght -56) * 0.1 / rowNum);
//			pixel = unit * 10;
//			zoomh = (float) ((lenght -56) * 0.07 / rowNum);
//			shifty = (int) ((lenght -56) * 20.0 / rowNum);
//			if(dir == 0 || dir == 2){
//				shiftx = (int) ((lenght -56) * 5.0 / rowNum);
//			}else 	if(dir == 1 || dir == 3){
//				shiftx =(int) ((lenght -56) * 15.0 / rowNum);
//			}
//		}
			
		//reconstruct points
		int i,j;
		if(dir == 0 || dir == 2)
			for(i = 0;i<=rowNum;i++)
				for(j = 0;j<=colNum;j++){
					points[i][j].x = j;
					points[i][j].y = i;

					points[i][j].x = (int) ((points[i][j].x* 1.0 + points[i][j].y * lean* 1.0) * zoomx * unit * 1.0);
					
					points[i][j].y *= zoomy * unit;
					points[i][j].x += shiftx;
					points[i][j].y += shifty;
				}
		else if(dir == 1 || dir == 3)
			for(i = 0;i<=rowNum;i++)
				for(j = 0;j<=colNum;j++){
					points[i][j].x = j;
					points[i][j].y = i;
					
					points[i][j].x = (int) ((points[i][j].x* 1.0 - points[i][j].y * lean* 1.0) * zoomx * unit * 1.0);
					
					points[i][j].y *= zoomy * unit;
					points[i][j].x += shiftx;
					points[i][j].y += shifty;

				}
		
		
		
		Color oldColor;
		//int i,j;
		oldColor = g.getColor();
		g.setColor(new Color(0, 0, 0));
		//if(isSmallSensor== true){
			if(dir == 0 || dir == 1){
				for(int ii = 0;ii<44;ii++)  //40 is rowNum in the small sensor
					for(int jj =0;jj<52;jj++){ //46 is colNum in the small sensor
						if(backgroundArray[ii][jj] == 1){
							
							//g.drawLine(points[ii+4][jj+3].x,points[ii+4][jj+3].y,points[ii+4][jj+3].x,points[ii+4][jj+3].y);
							g.drawLine(points[ii][jj].x,points[ii][jj].y,points[ii][jj].x,points[ii][jj].y);
							//pDC->SetPixel(points[ii][jj],RGB(0,0,0));
						}
						else if(backgroundArray[ii][jj] == 0 && jj != 0 && backgroundArray[ii][jj-1] == 1){
							
							
							//g.drawLine(points[ii+4][jj+3].x,points[ii+4][jj+3].y,points[ii+4][jj+3].x,points[ii+4][jj+3].y);
							g.drawLine(points[ii][jj].x,points[ii][jj].y,points[ii][jj].x,points[ii][jj].y);
							
						}
					}
				
				
			}
			else if(dir == 2 || dir ==3)
				for(int ii = 0;ii<44;ii++)  //the same
					for(int jj =0;jj<52;jj++){
						if(backgroundArray[43-ii][jj] == 1){ //39
							//g.drawLine(points[ii+1][jj+3].x,points[ii+1][jj+3].y,points[ii+1][jj+3].x,points[ii+1][jj+3].y);
							g.drawLine(points[ii+1][jj].x,points[ii+1][jj].y,points[ii+1][jj].x,points[ii+1][jj].y);
						}
						else if(backgroundArray[43-ii][jj] == 0 && jj != 0 && backgroundArray[43-ii][jj-1] == 1){
							//g.drawLine(ii * pixel, jj * pixel, ii * pixel, jj * pixel);
							//g.drawLine(points[ii+1][jj+3].x,points[ii+1][jj+3].y,points[ii+1][jj+3].x,points[ii+1][jj+3].y);
							g.drawLine(points[ii+1][jj].x,points[ii+1][jj].y,points[ii+1][jj].x,points[ii+1][jj].y);
						}
					}
//		}else{
//			//大传感器的坐标图
//		}
		g.setColor(oldColor);
		
				//CString str2;str2.Format(L"zomx:%d,zoomy:%d,float:%f",zoomx,zoomy,lean);
				//pDC->TextOut(0,0,str2);
				//System.out.println("zoomx:"+zoomx+",zoomy:"+zoomy+",lean:"+lean);
				if(dir == 0){
					int totalNum = rowNum * colNum;
					for(i = 0;i<rowNum;i++)
						for(j = colNum-1;j>=0;j--)
						//for(j = 0;j<colNum;j++)
							//drawCube(i,j,dd[i*colNum+j],g);
							drawCube(i,j,dd[totalNum-1-(j*rowNum+i)],g);
				}
				if(dir ==1){
					int totalNum = rowNum * colNum;
					for(i = 0;i<rowNum;i++)
						//for(j = 0;j<colNum;j++)
						for(j = 0;j<colNum;j++)
							drawCube(i,j,dd[totalNum-1-(j*rowNum+i)],g);
				}
				//AfxMessageBox(L"draw");

				if(dir == 2 ){
					//int totalNum = rowNum * colNum;
					for(i = 0;i<rowNum;i++)
						//for(j = 0;j<colNum;j++)
						for(j = colNum-1;j>=0;j--)
						//for(j = 0;j<colNum;j++)
							//drawCube(i,j,dd[totalNum-(i*colNum+j)-1],g);
							drawCube(i,j,dd[j*rowNum+i],g);
					//AfxMessageBox(L"draw");
				}

				if(dir == 3){
					//int totalNum = rowNum * colNum;
					for(i = 0;i<rowNum;i++)
						for(j = 0;j<colNum;j++)
							//drawCube(i,j,dd[totalNum-(i*colNum+j)-1],g);
							drawCube(i,j,dd[j*rowNum+i],g);
					//AfxMessageBox(L"draw");
				}
	}

	/**
	 * 画一个立方体
	 * @param i x坐标
	 * @param j y坐标
	 * @param b 数值
	 * @param g 画笔
	 */
	private void drawCube(int i, int j, int b, Graphics g) {
		// TODO Auto-generated method stub
		int dataColor = b;
		
		if(dataColor == 0)  //48 before
			return;
//		if (dataColor < 0)   
//			dataColor += 256;
		
		int x_1,y_1,x_2,y_2,x_3,y_3,x_4,y_4,x_5,y_5,x_6,y_6,x_7,y_7,x_8,y_8;

		x_2 = points[i][j].x;		y_2 = points[i][j].y;	
		x_1 = points[i][j+1].x;		y_1 = points[i][j+1].y;
		x_3 = points[i+1][j].x;		y_3 = points[i+1][j].y;
		x_4 = points[i+1][j+1].x;	y_4 = points[i+1][j+1].y;

		x_5 = x_1;		y_5 = (int) (y_1 - dataColor * zoomh);
		x_6 = x_2;		y_6 = (int) (y_2 - dataColor * zoomh);
		x_7 = x_3;		y_7 = (int) (y_3 - dataColor * zoomh);
		x_8 = x_4;		y_8 = (int) (y_4 - dataColor * zoomh);
			
		
		//开始绘图

		Color color = null;
		if(dataColor == 255)
			color = MyColor.values()[16].getRgb();
		else{
			int in = dataColor >>> 4;
			color = MyColor.values()[in].getRgb();
		}	
			
		
//		high = dataColor / 64;
//		low = dataColor % 64;
//		//System.out.println(low + "-->low");
//		switch (high) {
//		case 0: // g up
//			redColor = 0;
//			greenColor = 0 + low * 4;
//			blueColor = 255;
//			color = new Color(redColor, greenColor, blueColor);
//			break;
//		case 1: // b down
//			redColor = 0;
//			greenColor = 255;
//			blueColor = 255 - low * 4;
//			color = new Color(redColor, greenColor, blueColor);
//			break;
//		case 2: // r up
//			redColor = 0 + low * 4;
//			greenColor = 255;
//			blueColor = 0;
//			color = new Color(redColor, greenColor, blueColor);
//			break;
//		case 3: // g down
//			redColor = 255;
//			greenColor = 255 - low * 4;
//			blueColor = 0;
//			color = new Color(redColor, greenColor, blueColor);
//			break;
//		default:
//			break;
//
//		}
	
		//改进画法
		int ptx1[] = new int[6];
		int pty1[] = new int[6];
		Color oldColor = g.getColor();
		g.setColor(color);
		if(dir == 1 || dir ==3){
			
			ptx1[0]= x_1; pty1[0] = y_1;
			ptx1[1] = x_5; pty1[1] = y_5;
			ptx1[2] = x_6; pty1[2] = y_6;
			ptx1[3] = x_7; pty1[3] = y_7;
			ptx1[4] = x_3; pty1[4] = y_3;
			ptx1[5]= x_4; pty1[5] = y_4;
			
			
			g.fillPolygon(ptx1,pty1,6);
			
			g.setColor(new Color(0,0,0));
			g.drawPolygon(ptx1,pty1,6);
			g.drawLine(x_8, y_8, x_7, y_7);
			g.drawLine(x_8, y_8, x_5, y_5);
			g.drawLine(x_8, y_8, x_4, y_4);
			
		}else if(dir == 0 || dir == 2){
			
			ptx1[0]= x_2; pty1[0] = y_2;
			ptx1[1] = x_3; pty1[1] = y_3;
			ptx1[2] = x_4; pty1[2] = y_4;
			ptx1[3] = x_8; pty1[3] = y_8;
			ptx1[4] = x_5; pty1[4] = y_5;
			ptx1[5]= x_6; pty1[5] = y_6;			
			
			g.fillPolygon(ptx1, pty1, 6);
			
			g.setColor(new Color(0,0,0));	
			g.drawPolygon(ptx1,pty1,6);
			g.drawLine(x_7, y_7, x_6, y_6);
			g.drawLine(x_7, y_7, x_8, y_8);
			g.drawLine(x_7, y_7, x_3, y_3);
			
		}
		g.setColor(oldColor);
	}


	//响应鼠标事件
	class MyMouseAdapter extends MouseAdapter{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			//System.out.println("click");
			dir ++;
			dir = dir %4;
			
			switch(dir){
			case 0:
				shiftx /= 3.0;  //原来是4
				break;
			case 1:
				shiftx *= 3.0;
				break;
			case 2:
				shiftx /= 3.0;
				break;
			case 3:
				shiftx *= 3.0;
				break;
			default:break;
			}
			int i,j;
			if(dir == 0 || dir == 2)
				for(i = 0;i<=rowNum;i++)
					for(j = 0;j<=colNum;j++){
						points[i][j].x = j;
						points[i][j].y = i;

						points[i][j].x = (int)((points[i][j].x* 1.0f + points[i][j].y * lean* 1.0f) * zoomx * unit * 1.0f);
						
						points[i][j].y *= zoomy * unit;
						points[i][j].x += shiftx;
						points[i][j].y += shifty;
					}
					

			else if(dir == 1 || dir == 3)
				for(i = 0;i<=rowNum;i++)
					for(j = 0;j<=colNum;j++){
						points[i][j].x = j;
						points[i][j].y = i;

						points[i][j].x = (int)((points[i][j].x* 1.0f - points[i][j].y * lean* 1.0f) * zoomx * unit * 1.0f);
						
						points[i][j].y *= zoomy * unit;
						points[i][j].x += shiftx;
						points[i][j].y += shifty;

					}
			repaint();
		}

		
	}


	@Override
	public void onSize(int lenght, int flag) {
		// TODO Auto-generated method stub
		if(flag==0){
			unit = (float) ((lenght-8)  * 0.1 / colNum);
			pixel = unit * 10;
			zoomh = (float) ((lenght-8)  * 0.07 / colNum);
			shifty = (int) ((lenght-8) * 20.0 / colNum);
			if(dir == 0 || dir == 2){
				shiftx = (int) ((lenght-8) * 5.0 / colNum);
			}else 	if(dir == 1 || dir == 3){
				shiftx = (int) ((lenght-8) * 15.0 / colNum); 
			}
		}else if(flag ==1){
			unit = (float) ((lenght -56) * 0.1 / rowNum);
			pixel = unit * 10;
			zoomh = (float) ((lenght -56) * 0.07 / rowNum);
			shifty = (int) ((lenght -56) * 20.0 / rowNum);
			if(dir == 0 || dir == 2){
				shiftx = (int) ((lenght -56) * 5.0 / rowNum);
			}else 	if(dir == 1 || dir == 3){
				shiftx =(int) ((lenght -56) * 15.0 / rowNum);
			}
		}
			
		//reconstruct points
		int i,j;
		if(dir == 0 || dir == 2)
			for(i = 0;i<=rowNum;i++)
				for(j = 0;j<=colNum;j++){
					points[i][j].x = j;
					points[i][j].y = i;

					points[i][j].x = (int) ((points[i][j].x* 1.0 + points[i][j].y * lean* 1.0) * zoomx * unit * 1.0);
					
					points[i][j].y *= zoomy * unit;
					points[i][j].x += shiftx;
					points[i][j].y += shifty;
				}
		else if(dir == 1 || dir == 3)
			for(i = 0;i<=rowNum;i++)
				for(j = 0;j<=colNum;j++){
					points[i][j].x = j;
					points[i][j].y = i;
					
					points[i][j].x = (int) ((points[i][j].x* 1.0 - points[i][j].y * lean* 1.0) * zoomx * unit * 1.0);
					
					points[i][j].y *= zoomy * unit;
					points[i][j].x += shiftx;
					points[i][j].y += shifty;

				}
		
		repaint();
	}

	
	@Override
	public void stateChanged(ChangeEvent e) {
		DisplayModel dm = (DisplayModel)e.getSource();
		if(dm.getTowho() == 1) return;
		isStrange = dm.isStrange();
		if(isStrange);
		displayData = dm.getaFrame();
		frameIndex = dm.getFrameIndex();
		
		repaint();
		
	}




	

	
}
