package assignment3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import CatInfo; 


public class CatTree implements Iterable<CatInfo>{
    public CatNode root;
    
    public CatTree(CatInfo c) {
        this.root = new CatNode(c);
    }
    
    private CatTree(CatNode c) {
        this.root = c;
    }
    
    public void addCat(CatInfo c)
    {
        this.root = root.addCat(new CatNode(c));
    }
    
    public void removeCat(CatInfo c)
    {
        this.root = root.removeCat(c);
    }
    
    public int mostSenior()
    {
        return root.mostSenior();
    }
    
    public int fluffiest() {
        return root.fluffiest();
    }
    
    public CatInfo fluffiestFromMonth(int month) {
        return root.fluffiestFromMonth(month);
    }
    
    public int hiredFromMonths(int monthMin, int monthMax) {
        return root.hiredFromMonths(monthMin, monthMax);
    }
    
    public int[] costPlanning(int nbMonths) {
        return root.costPlanning(nbMonths);
    }
    
    
    
    public Iterator<CatInfo> iterator()
    {
        return new CatTreeIterator();
    }
    
    class CatNode {
        
        CatInfo data;
        CatNode senior;
        CatNode same;
        CatNode junior;
        
        public CatNode(CatInfo data) {
            this.data = data;
            this.senior = null;
            this.same = null;
            this.junior = null;
        }
        
        public String toString() {
            String result = this.data.toString() + "\n";
            if (this.senior != null) {
                result += "more senior " + this.data.toString() + " :\n";
                result += this.senior.toString();
            }
            if (this.same != null) {
                result += "same seniority " + this.data.toString() + " :\n";
                result += this.same.toString();
            }
            if (this.junior != null) {
                result += "more junior " + this.data.toString() + " :\n";
                result += this.junior.toString();
            }
            return result;
        }
        
        
        public CatNode addCat(CatNode c) {
            if (this != null) {
            	// if input cat is junior
            	if (c.data.monthHired > this.data.monthHired) {
            		if (this.junior == null) {
            			this.junior = c;
            		}
            		else {
            			this.junior = this.junior.addCat(c);
            		}
            	}
            	// if input cat is senior
            	else if (c.data.monthHired < this.data.monthHired) {
            		if (this.senior == null) {
            			this.senior = c;
            		}
            		else {
            			this.senior = this.senior.addCat(c);
            		}
            	}
            	// else compare by fur thickness if cats are same 
            	else {
            		// if input cat is less fluffy
            		if (c.data.furThickness < this.data.furThickness) {
            			if (this.same == null) {
            				this.same = c;
            			}
            			else {
            				this.same = this.same.addCat(c);
            			}
            		}
            		// if input cat is more fluffy
            		else if (c.data.furThickness > this.data.furThickness) {
            			if (this.same == null) {
            				this.same = new CatNode(this.data);
            				this.data = c.data;
            			}
            			else {
            				CatNode temp = new CatNode(this.data);
            				this.data = c.data;
            				this.same = this.same.addCat(temp);
            			}
            		}
            	}
            }
            return this; 
        }
        
        
        public CatNode removeCat(CatInfo c) {
            if (this != null) {
            	CatNode temp = this;
            	// input cat junior
            	if (c.monthHired > this.data.monthHired) {
            		if (this.junior != null) {
            			this.junior = this.junior.removeCat(c);
            			return temp;
            		}
            		return temp;
            	}
            	// input cat senior
            	else if (c.monthHired < this.data.monthHired) {
            		if (this.senior != null) {
            			this.senior = this.senior.removeCat(c);
            			return temp;
            		}
            		return temp;
            	}
            	// input cat at root
            	else if (this.data == c) {
            		if (this.same != null) {
            			this.same.senior = this.senior;
            			this.same.junior = this.junior;  
            			return this.same;
            		}
            		else if (this.same == null && this.senior != null) {
            			if (this.senior.junior != null) {
            				this.senior.junior.junior = this.junior;
            			}
            				this.senior.junior = this.junior;
            			return this.senior;
            		}
            		else if (this.same == null && this.senior == null) {
            			return this.junior;
            		}
            	}
            }
            return this;
        }
        
        
        public int mostSenior() {
        	int max = 0;
        	if (this != null) {
        		max = this.data.monthHired;
        		int rmax;         	
              	if (this.senior != null) {
              		rmax = this.senior.mostSenior();
              		max = Math.min(max, rmax);
              	}             	
              }        	
              return max;        	
          }
   
        
        public int fluffiest() {
        	int max = 0;
        	if (this != null) {
        		max = this.data.furThickness;
        		int lmax, rmax;
              	if (this.junior != null) {
              		lmax = this.junior.fluffiest();
              		max = Math.max(max, lmax);
              	}
              	if (this.senior != null) {
              		rmax = this.senior.fluffiest();
              		max = Math.max(max, rmax);
              	}
              }
              return max;
        }
        
        
        public int hiredFromMonths(int monthMin, int monthMax) {
        	if (monthMin > monthMax) {
        		return 0;
        	}
        	if (this != null) {      		
        		if (this.data.monthHired >= monthMin && this.data.monthHired <= monthMax) {
        			// count += 1; (dont need to initialize as 0)
        			int sameCount = 0;
        			int jrCount = 0;
        			int srCount = 0;
        			if (this.same != null) {
        				sameCount = this.same.hiredFromMonths(monthMin, monthMax);
        				// count += ^
        			}
        			if (this.junior != null) {
        				jrCount = this.junior.hiredFromMonths(monthMin, monthMax);
        				// count += ^
        			}
        			if (this.senior != null) {
        				srCount = this.senior.hiredFromMonths(monthMin, monthMax);
        				//count += ^
        			}   			
        			return 1 + sameCount + jrCount + srCount;  
        			// should have had else ifs so that it would work if cats in diff branches
        			// return count; (or maybe do 2 other cases and finally return count)
        		}
        		else if (this.data.monthHired < monthMin){
        			if (this.junior != null) {
        				return this.junior.hiredFromMonths(monthMin, monthMax);
        			}
        		}
        		else if (this.data.monthHired > monthMax){
        			if (this.senior != null) {
        				return this.senior.hiredFromMonths(monthMin, monthMax);
        			}
        		}       		
        	}
            return 0;            
        }
        
        public CatInfo fluffiestFromMonth(int month) {
            if (this != null) {
            	if (this.data.monthHired == month) {
            		return this.data;
            	}
            	else if (this.data.monthHired < month) {
            		return this.junior.fluffiestFromMonth(month);
            	}
            	else if (this.data.monthHired > month){
            		return this.senior.fluffiestFromMonth(month);
            	}
            }
            return null; 
        }
        
        public int[] costPlanning(int nbMonths) {       	
        	int[] intArray = new int[nbMonths];
        	costIterator(intArray);        		       		
       		return intArray; 
        }        
        
        // helper function for costPlanning 
        private void costIterator(int[] inputArray) {
        	if (this != null) {
        		if (this.same != null) {
        			this.same.costIterator(inputArray);
        		}
        		if (this.junior != null) {
        			this.junior.costIterator(inputArray);
        		}
        		if (this.senior != null) {
        			this.senior.costIterator(inputArray);
        		}   
        		int curInd = this.data.nextGroomingAppointment - 243;
        		if (curInd < inputArray.length) {
        			inputArray[curInd] += this.data.expectedGroomingCost;
        		}
        	}
        }     
    }
    
    private class CatTreeIterator implements Iterator<CatInfo> {
    	
    	// fields
    	public ArrayList<CatInfo> newList;
    	private int curInd = 0;
    	
    	// helper function
    	public ArrayList<CatInfo> catList(CatNode node) {
    	   ArrayList<CatInfo> newList = new ArrayList<CatInfo>();
    	   if (node == null)
    		   return newList;    	     	   
    	   if (node.senior != null)
    		   newList.addAll(catList(node.senior));
    	   if (node.same != null)
    		   newList.addAll(catList(node.same));
    	   newList.add(node.data);
    	   if (node.junior != null)
    		   newList.addAll(catList(node.junior));
    	   return newList;
    	}
    	
        
        public CatTreeIterator() {
        	
        	if (root == null)
        		throw new NoSuchElementException();
        	
        	this.newList = catList(root);
        	this.curInd = 0;
        }
        
        public CatInfo next(){
        	if (this.hasNext()) {
        		curInd += 1;
        		return newList.get(curInd);
        	}
        	else
        		return null;
        }
        
        public boolean hasNext() {
        	return (curInd != newList.size() - 1); 
        }
    	
    }
    
}

