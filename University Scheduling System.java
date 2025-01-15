
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FinalProject {

	public static void main(String[] args) throws FileNotFoundException, IdException {

		ArrayList<Person> peopleList = new ArrayList<>(); // creates the array to store all faculty, students, and TA's

		Scanner fileScan = new Scanner(System.in);
		Scanner scanner = null;
		String file = null;
		int i = 0;

		System.out.print("Enter the absolute path of the file: ");
		while (i == 0) { // loops until user enters a correct file address
			file = fileScan.nextLine();
			try { // scanner reads file lec.txt
				scanner = new Scanner(new File(file));
				i++;
			} catch (Exception e) {
				System.out.println("Sorry no such file");
				System.out.print("Try again: ");
			}

		}

		String line;
		String[] lecArray;
		int index = 0;
		int numLines = 0;
		
		while (scanner.hasNextLine()) { // Count number of lines in file
		    scanner.nextLine();
		    numLines++;
		}
		lecArray = new String[numLines];
		
		scanner = new Scanner(new File(file)); // Reset scanner to beginning of file
		
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			lecArray[index] = line; // splits lines by comma from lec.txt into an array
			index++;
		}
		
		System.out.println("File found! Let's proceed...");

		int j = 0;
		while (j == 0) { // loops until user exits

			System.out.println("*****************************");
			System.out.println("Choose one of these options: ");
			System.out.println("1 - Add a new Faculty to the schedule");
			System.out.println("2 - Enroll a Student to a Lecture");
			System.out.println("3 - Print the schedule of a Faculty");
			System.out.println("4 - Print the schedule of a TA");
			System.out.println("5 - Print the Schedule of a Student");
			System.out.println("6 - Delete a Lecture");
			System.out.println("7 - Exit");
			System.out.print("\t\t Enter your choice: ");

			Scanner scan = new Scanner(System.in);
			
			while (!scan.hasNextInt()) {
			    System.out.println("Invalid input! Please enter an integer.");
			    scan.next(); // clear the invalid input from the scanner
			}
			int choice = scan.nextInt();
			
			if (choice == 1) { // Adds faculty______________________________________________________________________________________________________________________________________
				
				int facultyID = 0;
				boolean validFacultyID = false;
				boolean hasID = false;
				while(!validFacultyID || hasID) { // loops until a valid 7 digit ID is entered
				    System.out.print("Enter UCF ID: ");
				    try {
				        facultyID = scan.nextInt();
				        Person.validateID(facultyID);
				        validFacultyID = true;

				        //checks to see if ID has been entered before
				        hasID = false;
				        for(Person s : peopleList) {
				            if(facultyID == s.getId()) {
				                hasID = true;
				                System.out.println("ID is already in use. Try again.");
				                break;
				            }
				        }
				    } 
				    catch (InputMismatchException e) {
				        System.out.println("Invalid input. Please enter a valid integer.");
				        scan.next(); // clears the scanner buffer
				    }
				    catch (IdException e) {
				        System.out.println(e.getMessage());
				    }
				}
				
				scan.nextLine();
				System.out.print("Enter name: ");
				String facultyName = scan.nextLine();

				String facultyRank = null;
				boolean rankIsValid = false;
				while(!rankIsValid) { //checks if user enters a valid rank
					System.out.print("Enter rank: ");
					facultyRank = scan.nextLine();
				
					if(facultyRank.equalsIgnoreCase("professor") || facultyRank.equalsIgnoreCase("associate professor") || facultyRank.equalsIgnoreCase("adjunct")) {
						rankIsValid = true;
					}
					else {
						System.out.println("Rank does not exist, try again.");
					}
				}

				System.out.print("Enter office location: ");
				String facultyOfficeLocation = scan.nextLine();
				
				int facultyNumLectures = 0;
				boolean validInput = false;

				while(!validInput) {
				    System.out.print("Enter the number of lectures: ");
				    try {
				        facultyNumLectures = scan.nextInt();
				        validInput = true;
				    }
				    catch (InputMismatchException e) {
				        System.out.println("Invalid input. Please enter a valid integer.");
				        scan.next(); // consume the invalid input
				    }
				}
				
				scan.nextLine();
				System.out.print("Enter the crns of the lectures to assign to this faculty: ");

				String facultyArr[] = null;
				String facultyCrns = null;
				int p = 0;
				while (p == 0) { // loops if a lecturer has already been entered before for this crn
					Boolean hasCrns = false;
					facultyCrns = scan.nextLine();
					facultyArr = facultyCrns.split(" ");
					
					for (Person s : peopleList) {
					    for (int n = 0; n < facultyArr.length; n++) {
					        for(int b = 0; b < s.crns.length; b++) {
					            if (facultyArr[n].equals(s.crns[b]) && s.getPeopleType().equals("lecturer")) {
					                System.out.println("Crn has already been assigned. Try again.");
					                hasCrns = true;					                					                
					            }
					        }
					    }
					    break;
					}
					if (hasCrns) {
					    continue;
					} 
					else {
					    break;
					}
				}
				
				String typeLecturer = "lecturer";
				peopleList.add(new Faculty(facultyID, facultyName, facultyRank, facultyOfficeLocation, facultyNumLectures, facultyCrns, typeLecturer));
			

				for (int n = 0; n < facultyArr.length; n++) {
					String crn = facultyArr[n];
					for (int k = 0; k < lecArray.length - 1; k++) {
						String[] splitLine = lecArray[k].split(","); // splits lines from lec.txt into an array

						if (crn.equals(splitLine[0])) {
							if (crn.equals(splitLine[0]) && splitLine.length > 5 && splitLine[6].equalsIgnoreCase("No")) { // lectures with no labs
								System.out.println("[" + lecArray[k] + "]" + " Added!");
							}

							else if (crn.equals(splitLine[0]) && splitLine.length > 6 && splitLine[6].equalsIgnoreCase("Yes")) { // lectures with labs
								System.out.println("[" + lecArray[k] + "]" + " has these labs: ");

								for (int m = k + 1; m < lecArray.length - 1; m++) { // loops to print labs
									String[] splitLab = lecArray[m].split(",");
									if (splitLab.length == 2) {
										System.out.println(lecArray[m]);
									} else {
										break;
									}
								}
								
								// assign a TA for each lab
								for (int m = k + 1; m < lecArray.length - 1; m++) { // loops to print labs
									String[] splitLab = lecArray[m].split(",");
									if (splitLab.length == 2) {

										String TACrns = splitLab[0];
										
										int TAID = 0;
										boolean validTAID = false;
										boolean hasID2 = false;
										boolean alreadyStudent = false;
										while(!validTAID || hasID2 || alreadyStudent) {
											System.out.print("Enter UCF ID for TA: ");
											try {
												TAID = scan.nextInt();
												
												Person.validateID(TAID);
												validTAID = true;
												hasID2 = false;
												alreadyStudent = false;
												for(Person s : peopleList) { //TAs may teach multiple labs and may be a student
													int q = 0;
													while ( q < s.crns.length) {
														if(TAID == s.getId() && s.getPeopleType().equals("lecturer")){
															hasID2 = true;
															System.out.println("ID is already in use by a faculty. Try again.");
															break;
															
														}
														else if( TAID == s.getId() && s.getPeopleType().equals("student") && s.crns[q].equals(crn) && !hasID2 ) {
																														
															alreadyStudent = true;
															System.out.println("Already student for a lab of this lecture.");
															q++;
														}
														else
															q++;
													}
											    }
												} 
											catch (InputMismatchException e) {
											        System.out.println("Invalid input. Please enter a valid ID.");
											        scan.next(); // clears the invalid input from the scanner
											    }
											catch (IdException e) {
												System.out.println(e.getMessage());
											}
										}		
										scan.nextLine();
										
										String TAName = null;
										String supervisorName = null;
										String degreeSeeking = null;
										boolean TAExists = false;
										String labLectureCrn = crn;

										for (Person r : peopleList) { // loops through peopleList array to identify TAs 
											if (TAID == (r.getId()) && (r.getPeopleType().equals("TA") || (r.getPeopleType().equals("student") && !r.getCrns().equals(crn))) && !alreadyStudent) {
												System.out.println("TA found: " + r.getName());
												TAName = r.getName();
												TAExists = true;
												break;
											}					
										}
										

										if (TAExists == false) {
											System.out.print("Name of TA: ");
											TAName = scan.nextLine();
	
											System.out.print("TA's supervisor's name: ");
											supervisorName = scan.nextLine();
	
											System.out.print("Degree Seeking: ");
											degreeSeeking = scan.nextLine();
											
										}
										
										String typeTA = "TA";
										peopleList.add(new TA(TAID, TAName, TACrns, supervisorName, degreeSeeking, typeTA, labLectureCrn));

									} 
									else {
										break;
									}
								}
								System.out.println("[" + lecArray[k] + "]" + " Added!");
							}

							else { // online lectures
								System.out.println("[" + lecArray[k] + "]" + " Added!");
							}
						}
					}
				}
								
				// adds a faculty to peopleList array
				typeLecturer = "lecturer";
				peopleList.add(new Faculty(facultyID, facultyName, facultyRank, facultyOfficeLocation, facultyNumLectures, facultyCrns, typeLecturer));
			}

			else if (choice == 2) { // Enrolls a student to a lecture________________________________________________________________________________________________________________
				int studentID = 0;
				boolean validStudentID = false;
				boolean hasID = false;
				boolean foundRecord = false;
				String studentName = null;
				String labCrn = null;

				while(!validStudentID || hasID) {
				    System.out.print("Enter UCF ID: ");
				    try {
				        studentID = scan.nextInt();
				        Person.validateID(studentID);
				        validStudentID = true;
				        
				        hasID = false;
				        foundRecord = false;
				        for(Person s : peopleList) {
				            if(studentID == s.getId() && !s.getPeopleType().equals("TA")) {
				                hasID = true;
				                System.out.println("ID is already in use. Try again.");
				                break;
				            } else if(studentID == s.getId() && s.getPeopleType().equals("TA")) {
				                System.out.println("Record found: " + s.getName());
				                foundRecord = true;
				                studentName = s.getName();
				                break;
				            }
				        }
				    }
					catch (InputMismatchException e) {
				        System.out.println("Invalid input. Please enter a valid ID.");
				        scan.next(); // clears the invalid input from the scanner
				    }
				    catch (IdException e) {
				        System.out.println(e.getMessage());
				    }
				}
				scan.nextLine();

				if(!foundRecord) {
				    System.out.print("Enter name: ");
				    studentName = scan.nextLine();
				}
				
				System.out.print("Enter the crns of the lectures: ");
				String studentArr[] = null;
				String studentCrns = null;
				studentCrns = scan.nextLine();
				
				for (int k = 0; k < lecArray.length - 1; k++) {
					
					studentArr = studentCrns.split(" ");
					
					String[] splitLine = lecArray[k].split(","); // splits lines from lec.txt into an array
					Boolean hasTAForLecture = false;
					
					Boolean hasPrefix = false;
					
					for (Person h : peopleList) { // loops through peopleList array to identify TAs from student entries
						for(int b = 0; b < h.crns.length; b++) {
							if ( studentID == h.getId() && h.getPeopleType().equals("TA") && studentCrns.equals(TA.getLabLectureCrn()) ) { // makes sure student is not assigned as a TA for this lecture
								hasTAForLecture = true;
							}
						}
					}		
					
					if (hasTAForLecture) {
						System.out.println("Student already TA for this lecture");
						break;
					}
					for (int n = 0; n < studentArr.length; n++) { 
						if (studentArr[n].equals(splitLine[0]) && splitLine.length > 6 && splitLine[6].equalsIgnoreCase("Yes") && !hasTAForLecture) { // lectures with labs
							for (int b = k + 1; b < lecArray.length - 1; b++) { // loops through labs
								String[] splitLab = lecArray[b].split(",");
	
								if (splitLab.length == 2) { // assigns student to random lab
																		
									String[] labArr = new String[3]; // 3 labs
	
									labArr[0] = lecArray[b];
									labArr[1] = lecArray[b + 1];
									labArr[2] = lecArray[b + 2];
	
									int randIndex = (int )(Math.random() * 3);
	
									String randLab = labArr[randIndex];
	
									System.out.println(studentName + " is added to lab : " + randLab + " and lecture " + lecArray[k]);									
									labCrn = randLab;
									break;
								}
							}
						}
					}
				}
				
				for (int k = 0; k < lecArray.length - 1; k++) {
					String[] splitLine = lecArray[k].split(",");
					
					for (int n = 0; n < studentArr.length; n++) { 
						if (studentArr[n].equals(splitLine[0]) && (splitLine.length > 5) && splitLine[6].equalsIgnoreCase("No")){ // lectures with no labs
							System.out.println("[" + lecArray[k] + "]" + " Added!");
						}
						else if(studentArr[n].equals(splitLine[0]) && splitLine.length == 5)//onine lectures
							System.out.println("[" + lecArray[k] + "]" + " Added!");
					}
				}
				 
				String typeStudent = "student";
				
				if(labCrn == null) {
					peopleList.add(new Student(studentID, studentName, studentCrns, typeStudent, labCrn));
				}
				else {
					peopleList.add(new Student(studentID, studentName, studentCrns, typeStudent, labCrn));
				}
			}

			else if (choice == 3) { // Prints the schedule of a faculty________________________________________________________________________________________________________________
				System.out.print("Enter UCF id of the faculty: ");
				
				int idFaculty = 0;
				try {
				    idFaculty = scan.nextInt();
				} catch (InputMismatchException e) {
				    scan.next();
				}				
				
				boolean foundFaculty = false;

				for (Person t : peopleList) {
				    if (idFaculty == t.getId() && t.getPeopleType().equals("lecturer")) {
				        System.out.println(t.getName() + " is teaching the lectures:");
				        for (int k = 0; k < lecArray.length - 1; k++) {
				            String[] splitLine = lecArray[k].split(",");
				            int q = 0;
				            while (q < t.crns.length) {
				                if (splitLine[0].equals(t.crns[q])) {
				                    System.out.println("[" + lecArray[k] + "]");
				                    q++;
				                } else {
				                    q++;
				                }
				            }
				        }
				        foundFaculty = true;
				        break; // exit the loop once the ID is found
				    } else if (idFaculty != t.getId() && (t.getPeopleType().equals("TA") || t.getPeopleType().equals("student"))) {
				        // skip over TAs and students in people list
				    }
				}

				if (!foundFaculty) {
				    System.out.println("No existing faculty with ID entered.");
				}
			}
			
			else if (choice == 4) { // Prints the schedule of a TA________________________________________________________________________________________________________________
			    System.out.print("Enter UCF id of the TA: ");
			    
			    int idTA = 0;
			    try {
			        idTA = scan.nextInt();
			    } catch (InputMismatchException e) {
			        scan.next(); // clear the invalid input from the scanner
			    }
			    
			    boolean foundTA = false;
			    
			    for(Person t : peopleList) {
			        if(idTA == t.getId() && t.getPeopleType().equals("TA")) {    
			            System.out.print(t.getName() + " is teaching the lab");
			            for (int k = 0; k < lecArray.length - 1; k++) {
			                String[] splitLine = lecArray[k].split(",");
			 
			                int q = 0;
			                while (q < t.crns.length) {
			                    if (splitLine[0].equals(t.crns[q])) {
			                        System.out.println("[" + lecArray[k] + "]");
			                        q++;
			                    } else {
			                        q++;
			                    }                       
			                }
			            }
			            foundTA = true;
			        }
			        else if(idTA != t.getId() && (t.getPeopleType().equals("lecturer") || t.getPeopleType().equals("student"))) {
			        	//do nothing to skip
			        }
			    }
			    
			    if (!foundTA && !peopleList.isEmpty()) {
			        System.out.println("No existing TA with ID entered.");
			    } else if (!foundTA && peopleList.isEmpty()) {
			        System.out.println("No existing TA with ID entered.");
			    }
			}

			else if (choice == 5) { // Prints the schedule of a student
			    System.out.print("Enter UCF id of the Student: ");
			    
			    int idStudent = 0;
			    try {
			        idStudent = scan.nextInt();
			    } catch (InputMismatchException e) {
			        scan.next(); // clear the invalid input from the scanner
			    }
			    
			    boolean foundStudent = false;
			    
			    for (Person t : peopleList) {
			        if (idStudent == t.getId() && t.getPeopleType().equals("student")) {
			            System.out.println(t.getName() + " is enrolled in: ");
			            for (int k = 0; k < lecArray.length - 1; k++) {
			                String[] splitLine = lecArray[k].split(",");
			                
			                int q = 0;
			                while (q < t.crns.length) {
			                    if (splitLine[0].equals(t.crns[q])) {
			                        System.out.println("[" + lecArray[k] + "]");
			                        
			                        if(Student.getLabCrn() != null && !Student.getLabCrn().equals("")) {
			                        	
			                        	for(int p = 0; p < Student.getLabCrn().length; p++) {
			                        					                        		
			                        		if(splitLine[0].equals(t.crns[p])) {
				                            	System.out.println("lab: " + Student.getLabCrn()[p]);
				                        	}
			                        	}
			                        }
			                        
			                        q++;
			                    } else {
			                        q++;
			                    }
			                }
			            }
			            foundStudent = true;
			            break; // break out of the for loop when student is found
			        } else if (idStudent != t.getId() && (t.getPeopleType().equals("lecturer") || t.getPeopleType().equals("TA"))) {
			            System.out.print("");
			        } 
			    }
			    
			    if (!foundStudent) {
			        System.out.println("No existing Student with ID entered.");
			    }
			}

			else if (choice == 6) { // Deletes a scheduled lecture
			    System.out.print("Enter the crn of the lecture to delete: ");
			    String crnDelete = scan.next();
			    
			    
			}
			else if (choice == 7) { // Exits program___________________________________________________________________________________________________________________________
					if(lecArray.length < 26){
						System.out.println("You have made a deletion of at least one lecture. Would you like to print the copy of lec.txt?");
						System.out.print("Enter y/Y for yes or n/N for no: ");
						String answer = scan.next();
						if(answer.equalsIgnoreCase("y")) {
							for (int j2 = 0; j2 < lecArray.length; j2++) {
							 System.out.println(lecArray[j2]);
							 j++;
							}
							System.out.println("Bye!");
							 j++;
						}
						else if(answer.equalsIgnoreCase("n")) {
							System.out.println("Bye!");
							j++;
						}
					}
					else {
						System.out.println("No lectures have been deleted.");
						System.out.println("Bye");
						j++;
					}
				}
			else {
				System.out.println("Invalid selection");
				System.out.println("Try again");
			}
		}
	}
}

abstract class Person { //_______________________________________________________________________________________________________________________________________________________________________

	ArrayList<Person> peopleList = new ArrayList<>(); // creates the array to store all faculty, students, and TA's

	// attributes that all faculty, students, and TA's share
	protected int id;
	protected String name;
	protected String[] crns;
	protected String[] crnArr;
	protected String peopleType;
	
	public Person(int id, String name, String crns, String peopleType) throws IdException{
		String[] crnArr = crns.split(" ");
		this.id = id;
		this.name = name;
		this.crns = crnArr;
		this.peopleType = peopleType;
	}
	
	public static void validateID(int id) throws IdException{ //this method is used to validate ID length
		if (Integer.toString(id).length() != 7) { // throws IdException of id entered is not 7 digits
			throw new IdException(id);
		}
	}

	// Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getCrns() {
		return crns;
	}

	public void setCrns(String crns) {
		String[] crnArr = crns.split(" ");
		this.crns = crnArr;
	}
	

	public String getPeopleType() {
		return peopleType;
	}

	public void setPeopleType(String peopleType) {
		this.peopleType = peopleType;
	}

	@Override
	public String toString() {
		return id + " " + name + " " + crns + " ";
	}

}

class IdException extends Exception { //____________________________________________________________________________________________________________________________________________________________

	public IdException(int id) {
		super(">>>>>>Sorry incorrect format. (Ids are 7 digits)");
	}
}

class Faculty extends Person { //_______________________________________________________________________________________________________________________________________________________________________
	private String rank;
	private String officeLocation;
	private int numLectures;

	public Faculty(int id, String name, String rank, String officeLocation, int numLectures, String crns, String peopleType) throws IdException {
		super(id, name, crns, peopleType); // uses abstract class Person
		this.rank = rank;
		this.officeLocation = officeLocation;
		this.numLectures = numLectures;
	}

	// setters and getters
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getOfficeLocation() {
		return officeLocation;
	}

	public void setOfficeLocation(String officeLocation) {
		this.officeLocation = officeLocation;
	}

	public int getNumLectures() {
		return numLectures;
	}

	public void setNumLectures(int numLectures) {
		this.numLectures = numLectures;
	}

	public String toString() {
		return super.toString() + rank + " " + officeLocation + " " + numLectures + " " + " ";
	}
}

class Student extends Person {
	private static String[] labCrn;
	private static String[] labArr = new String[0];;

	public Student(int id, String name, String crns, String peopleType, String labCrn) throws IdException {
		super(id, name, crns, peopleType); // uses abstract class Person
		
		if(labCrn != null) {
			String[] labArr = labCrn.split(" ");
			this.labCrn = labArr;
		}
		else {
			this.labCrn = labArr;
		}
	}

	//setters and getters

	public static String[] getLabCrn() {
		return labCrn;
	}

	public static void setLabCrn(String labCrn) {
		String[] labArr = labCrn.split(" ");
		Student.labCrn = labArr;
	}		
}

class TA extends Person { //__________________________________________________________________________________________________________________________________________________________________________
	private String supervisorName;
	private String degreeSeeking;
	private static String labLectureCrn;

	public TA(int id, String name, String crns, String supervisorName, String degreeSeeking, String peopleType, String labLectureCrn) throws IdException {
		super(id, name, crns, peopleType);
		this.supervisorName = supervisorName;
		this.degreeSeeking = degreeSeeking;
		this.labLectureCrn = labLectureCrn;
	}

	// getters and setters
	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}

	public String getDegreeSeeking() {
		return degreeSeeking;
	}

	public void setDegreeSeeking(String degreeSeeking) {
		this.degreeSeeking = degreeSeeking;
	}
	
	public static String getLabLectureCrn() {
		return labLectureCrn;
	}

	public void setLabLectureCrn(String labLectureCrn) {
		this.labLectureCrn = labLectureCrn;
	}

	public String toString() {
		return super.toString() + supervisorName + " " + degreeSeeking + " ";
	}
}
