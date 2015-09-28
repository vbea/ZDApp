package com.binxin.zdapp.wheel;


/**
 * Numeric Wheel adapter.
 */
public class StrericWheelAdapter implements WheelAdapter {
	
	/** The default min value */
	private String[] strContents;
	/**
	 * 
	 * @param strContents
	 */
	public StrericWheelAdapter(String[] strContents){
		this.strContents=strContents;
	}
	
	
	public String[] getStrContents() {
		return strContents;
	}


	public void setStrContents(String[] strContents) {
		this.strContents = strContents;
	}


	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			return strContents[index];
		}
		return null;
	}
	
	public int getItemsCount() {
		return strContents.length;
	}
	/**
	 * 璁剧疆鏈�ぇ鐨勫搴�
	 */
	public int getMaximumLength() {
		int maxLen=5;
		return maxLen;
	}
}
