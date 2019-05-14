import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Character;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.stream.Collectors;
import java.lang.StringBuilder;

class sudoku_solver{
	List<Cell> allCells = new ArrayList<>();
	List<Cell> backupCells = new ArrayList<>();
	String sudokuString;

	int[][] field = new int[9][9];
	
	public static void main(String[] args){
		sudoku_solver sudoku = new sudoku_solver();
		System.out.println("Initial State:");
		sudoku.printField();
		
		int emptyCells=sudoku.checkEmpty();
		int countFilled=0;
		boolean solved = false;
		
		while (emptyCells>0){
			sudoku.checkOptions();
			
		}
		
		System.out.println("Final State:");
		sudoku.printField();
	}
	
	public sudoku_solver(){
		int wrongInput=0;
		for (wrongInput=0; wrongInput<3; wrongInput++){
			boolean valid = this.validInput();
			if (valid==true){
				wrongInput=3;
			}
		}
		if (wrongInput==3){
			System.exit(0);
		}
		else {
			this.makeField(this.sudokuString);
		}
	}
	
	public boolean validInput(){
		boolean valid = false;
		System.out.println("Play with own sudoku or standard sudoku? (answer with 'own' or 'standard')");
		Scanner cardSet = new Scanner(System.in);
		String answer = cardSet.next();
		if (answer .equals("own")){
			System.out.println("Enter numbers for start sudoku (enter 0 for empty cell).");
			System.out.println("Enter all numbers left to right, top to bottom, without spacing.");
			System.out.println("i.e. 000820090500000000308040007100000040006402503000090010093004000004035200000700900");
			Scanner userInp = new Scanner(System.in);
			this.sudokuString = userInp.next();
		}
		else if (answer .equals("standard")){
			System.out.println("Standard sudoku inserted");
			this.sudokuString = "000820090500000000308040007100000040006402503000090010093004000004035200000700900";
		}
		else {
			System.out.println("Not a valid answer. Program closes (restart to play)");
			System.exit(0);
		}
		if (sudokuString.length() == 81){
			for (int i=0; i<81; i++){
				char ch_i = sudokuString.charAt(i);
				if (Character.isDigit(ch_i)){
					valid = true;
				}
			}
		}
		else if (sudokuString.length() != 81){
			System.out.println("Invalid input, number of numbers should be 81, but is " + sudokuString.length());
		}
		else {
			System.out.println("Invalid input, only use numbers!");
		}
		return valid;
	}
	
	void makeField(String sudokuString){
		int y = 0;
		for (int i=0; i<81; i++){
			Cell cell = new Cell(Character.getNumericValue(sudokuString.charAt(i)));
			if (i>0 && i%9==0){
				y = y+1;
			}
			//set x and y coordinates cells
			cell.setYCoord(y);
			cell.setXCoord(i%9); 
			//set x and y coordinates blocks
			if (y >= 0 && y<3){
				cell.setYBlock(0);
			}
			else if (y < 6){
				cell.setYBlock(1);
			}
			else if (y<9){
				cell.setYBlock(2);
			}
			if (i%9 >= 0 && i%9<3){
				cell.setXBlock(0);
			}
			else if (i%9 < 6){
				cell.setXBlock(1);
			}
			else if (i%9<9){
				cell.setXBlock(2);
			}
			allCells.add(cell);
		}
		
	}
	
	void printField(){
		System.out.println("-------------------------");
		for (int y=0; y<9; y++){
			for (int x=0; x<9; x++){
				if (x==8){
					for (int cellNR=0; cellNR<81; cellNR++){
						if (this.allCells.get(cellNR).getYCoord()==y &&
							this.allCells.get(cellNR).getXCoord()==x){
							System.out.print(this.allCells.get(cellNR).getPrintValue() + " |");	
						}
					}
				}
				else if (x%3==0){
					for (int cellNR=0; cellNR<81; cellNR++){
						if (this.allCells.get(cellNR).getYCoord()==y &&
							this.allCells.get(cellNR).getXCoord()==x){
							System.out.print("| " + this.allCells.get(cellNR).getPrintValue() + " ");	
						}
					}
				}
				else {
					for (int cellNR=0; cellNR<81; cellNR++){
						if (this.allCells.get(cellNR).getYCoord()==y &&
							this.allCells.get(cellNR).getXCoord()==x){
							System.out.print(this.allCells.get(cellNR).getPrintValue() + " ");	
						}
					}
				}
			}
			System.out.println("");
			if ((y+1)%3==0){
			System.out.println("-------------------------");
			}
		}
	}
	
	public int checkOptions(){
		System.out.println("Fill based on rows, collumns and blocks...");
		boolean noOptions=false;
		int countFilled = 0;
		for (int cellNR=0; cellNR<81; cellNR++){
			this.allCells.get(cellNR).options.clear();
			if (this.allCells.get(cellNR).getValue() == 0){	
				//x and y coordinates of this cell:
				int ycoord = this.allCells.get(cellNR).getYCoord();
				int xcoord = this.allCells.get(cellNR).getXCoord();
				int yBlock = this.allCells.get(cellNR).getYBlock();
				int xBlock = this.allCells.get(cellNR).getXBlock();
				for (int opt=1; opt<10; opt++){
					if ((this.inRow(opt, ycoord, xcoord) == false) &&
						(this.inCol(opt, ycoord, xcoord) == false) &&
						(this.inBlock(opt, yBlock, xBlock) == false)){
						this.allCells.get(cellNR).options.add(opt);
					}
				}
				if (this.allCells.get(cellNR).options.size()==1){
					this.allCells.get(cellNR).setValue(this.allCells.get(cellNR).options.get(0));
					countFilled = countFilled + 1;
				}
				else if (this.allCells.get(cellNR).options.size()==0){
					System.out.println("no options");
					cellNR=81;
					noOptions=true;
				}
			}	
		}
		
		if (noOptions==false){
			if (this.checkEmpty() == 0){
				System.out.println("Solved: ");
				this.printField();
				System.exit(0);	
			}
			else {
				if (countFilled > 0){
					this.checkOptions();
				}
				else if (countFilled == 0){
					this.crossrefOptions();
				}	
			}
		}
		return countFilled;
	}

	
	public boolean inRow(int opt, int y, int x){
		boolean inrow = false;
		for (int i=0; i<9; i++){
			for (int cellNR=0; cellNR<81; cellNR++){
				if (this.allCells.get(cellNR).getYCoord() == y &&
					this.allCells.get(cellNR).getXCoord() == i &&
					this.allCells.get(cellNR).getValue() == opt){
						inrow = true;
				}
			}
		}
		return inrow;
	}
	public boolean inCol(int opt, int y, int x){
		boolean incol = false;
		for (int i=0; i<9; i++){
			for (int cellNR=0; cellNR<81; cellNR++){
				if (this.allCells.get(cellNR).getYCoord() == i &&
					this.allCells.get(cellNR).getXCoord() == x &&
					this.allCells.get(cellNR).getValue() == opt){
						incol = true;
				}
			}
		}
		return incol;
	}
	public boolean inBlock(int opt, int yBlock, int xBlock){
		boolean inblock = false;
		for (int cellNR=0; cellNR<81; cellNR++){
			if (this.allCells.get(cellNR).getYBlock() == yBlock &&
				this.allCells.get(cellNR).getXBlock() == xBlock){
				if (this.allCells.get(cellNR).getValue() == opt){
					inblock = true;
				}
			}
		}
		return inblock;
	}
	
	public int checkEmpty(){
		int emptyCells = 0;
		for (int cellNR=0; cellNR<81; cellNR++){
			if (this.allCells.get(cellNR).getValue() == 0){
				emptyCells=emptyCells+1;
			}
		}
		System.out.println("empty cells: " + emptyCells);
		return emptyCells;
	}
	
	public int crossrefOptions(){
		System.out.println("Start cross referencing...");
		int countFilled=0;
		for (int cellNR=0; cellNR<81; cellNR++){
			if (this.allCells.get(cellNR).getValue()==0){
			
				int y = this.allCells.get(cellNR).getYCoord();
				int x = this.allCells.get(cellNR).getXCoord();
				int yBlock = this.allCells.get(cellNR).getYBlock();
				int xBlock = this.allCells.get(cellNR).getXBlock();
				List<Integer> options = new ArrayList<>();
				options = this.allCells.get(cellNR).options;
				for (int i=0; i<options.size(); i++){
					if(this.inRowOptions(options.get(i), y, x, cellNR) == false){
						this.allCells.get(cellNR).setValue(options.get(i));
						countFilled=countFilled+1;
					}
					else if(this.inColOptions(options.get(i), y, x, cellNR) == false){
						this.allCells.get(cellNR).setValue(options.get(i));
						countFilled=countFilled+1;
					}
					else if(this.inBlockOptions(options.get(i), yBlock, xBlock, cellNR) == false){
						this.allCells.get(cellNR).setValue(options.get(i));
						countFilled=countFilled+1;
					}
				}
			}
		}
		System.out.println("Filled: " + countFilled);
		System.out.println("In Between State: ");
		this.printField();
		this.checkEmpty();
		
		if (countFilled==0){
			System.out.println("No options found by cross referencing rows, cols and blocks");
		}
		else if (countFilled > 0){
			this.checkOptions();
		}
		int emptyCells = this.checkEmpty();
		return emptyCells;
	}
	
	public boolean inRowOptions(int opt, int y, int x, int refNR){
		boolean inrowoptions = false;
		for (int cellNR=0; cellNR<81; cellNR++){
			if (this.allCells.get(cellNR).getXCoord() != x && 
				this.allCells.get(cellNR).getYCoord() == y ){//&&
				if (this.allCells.get(cellNR).options.contains(opt)==true){
					inrowoptions = true;
					cellNR = 81;
				}
			}
		}
		return inrowoptions;
	}
	public boolean inColOptions(int opt, int y, int x, int refNR){
		boolean incoloptions = false;
		for (int cellNR=0; cellNR<81; cellNR++){
			if (this.allCells.get(cellNR).getYCoord() != y && 
				this.allCells.get(cellNR).getXCoord() == x ){//&&
				if (this.allCells.get(cellNR).options.contains(opt)==true){
					incoloptions = true;
					cellNR = 81;
				}
			}
		}
		return incoloptions;
	}
	public boolean inBlockOptions(int opt, int yBlock, int xBlock, int refNR){
		boolean inblockoptions = false;
		for (int cellNR=0; cellNR<81; cellNR++){
			if (cellNR != refNR &&
				this.allCells.get(cellNR).getYBlock() == yBlock &&
				this.allCells.get(cellNR).getXBlock() == xBlock ){//&&
				if (this.allCells.get(cellNR).options.contains(opt)){
					inblockoptions = true;
					cellNR = 81;
				}
			}
		}
		return inblockoptions;
	}
}