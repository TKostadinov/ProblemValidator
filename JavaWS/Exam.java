import java.util.Scanner;


public class Exam {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		byte day = input.nextByte();
		byte month = input.nextByte();
		short year = input.nextShort();
		
		if(day == 30 && (month == 4 || month == 6 || month == 9 || month == 11)) {
			day = 1;
			month++;
		} else if( day == 28 && month == 2) {
			day = 1;
			month++;
		} else if (day == 31 && (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10)) {
			day =1;
			month++;
		} else if (day == 31 && month == 12) {
			day = 1;
			month = 1;
			year++;
			
		} else {
			day++;
			
		}
 System.out.println(day+ "." + month + "." + year);
	}

}
