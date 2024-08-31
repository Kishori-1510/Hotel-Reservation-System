import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static  void reserveRoom( Connection connection, Scanner sc){
     try{
         System.out.println("Enter the guest Name:");
         String guestName=sc.next();
         sc.nextLine();
         System.out.println("Enter the Room No:");
         int roomNo=sc.nextInt();
         System.out.println("Enter the Contact No: ");
         String  contactNo=sc.next();

         String query="INSERT INTO reservation(guest_name,room_no,contact_no) " +
                 "values( ' " +guestName  +" '  ,  " + roomNo + " , ' "+contactNo+ " ')";
         try(Statement stmt=connection.createStatement()){
              int affectedrow=stmt.executeUpdate(query);
              if(affectedrow>0){
                  System.out.println("Reservation Successfull");
              }else{
                  System.out.println("Reservation Failed");
              }
         }
     }catch(Exception e){
        e.printStackTrace();
     }
    }
    public static void  viewReservations( Connection connection) throws  SQLException{
        String query="select * from reservation";
        try(Statement stmp=connection.createStatement()){
            ResultSet rs= stmp.executeQuery(query);
            System.out.println("Reservation record:");
            System.out.println("=====================================================================================");
            System.out.println("Reservation_ID \t Guest Name \t Room NO \t Contact NO \t Reservation_Date");
            System.out.println("=====================================================================================");

            while(rs.next())
            {
                int reservationid= rs.getInt("reservation_id");
                String guestName=rs.getString("guest_name");
                int roomNo=rs.getInt("room_no");
                String ContactNo=rs.getString("contact_no");
                String date=rs.getTimestamp("reservation_date").toString();

                System.out.println(reservationid+"\t\t\t\t"+guestName+"\t\t\t"+roomNo+"\t\t\t"+ContactNo+"\t\t\t"+date+" ");
            }
            System.out.println("=====================================================================================");
        }
    }

    public static  void getRoomNumber( Connection connection,Scanner sc){
     try{
         System.out.println("Enter reservation Id:");
         int id=sc.nextInt();
         String query="SELECT room_no FROM reservation WHERE reservation_id ="  + id +" ";
             Statement stmt=connection.createStatement();
             ResultSet rs=stmt.executeQuery(query);

             if(rs.next()){
                 int roomNo=rs.getInt("room_no");

                 System.out.println("Room no for reservation id is "+id+ "  is "+roomNo);
             }else{
                 System.out.println("Reservation is not found for the given id and Name");
             }


     }catch(Exception e){
         e.printStackTrace();
     }
    }
    public static  void updateReservation( Connection connection, Scanner sc){
      try{
          System.out.println("Enter the reservation id to update:");
          int reservationID=sc.nextInt();
          sc.nextLine();

          if(!reservationExist(connection,reservationID)){
              System.out.println("Reservation is not found");
              return;
          }
          System.out.println("Enter  new  guest Name:");
          String newguestName=sc.next();
          sc.nextLine();
          System.out.println("Enter new Room No:");
          int newRoomNo=sc.nextInt();
          System.out.println("Enter new Contact No: ");
          String  newContactNo=sc.next();

          String query="UPDATE reservation SET guest_name = ' " + newguestName + " ', " +
                  "room_no = "+ newRoomNo+ " ," +
                  "contact_no = ' " +newContactNo+" ' "+
                  "WHERE reservation_id = " + reservationID;

          try(Statement stmt=connection.createStatement()){
              int affectedrow=stmt.executeUpdate(query);
              if(affectedrow>0){
                  System.out.println("Reservation updated  Successfull");
              }else{
                  System.out.println("Reservation update Failed");
              }
          }
      }
      catch (Exception e){
          e.printStackTrace();
      }



    }
    public static  void deleteReservation( Connection connection, Scanner sc){
        try{
            System.out.println("Enter the reservation id to delete :");
            int reservationID=sc.nextInt();

            if(!reservationExist(connection,reservationID)){
                System.out.println("Reservation is not found");
                return;
            }
            String query="DELETE FROM reservation WHERE reservation_id= "+reservationID;
            try(Statement stmt=connection.createStatement()){
                int affectedrow=stmt.executeUpdate(query);
                if(affectedrow>0){
                    System.out.println("Reservation deleted  Successfull");
                }else{
                    System.out.println("Reservation deleted Failed");
                }
            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private  static boolean reservationExist(Connection connection,int reservationID){
        try{
            String query="SELECT reservation_id FROM reservation WHERE reservation_id = " + reservationID;
            try(Statement stmt=connection.createStatement();
                ResultSet rs= stmt.executeQuery(query))
            {
                return  rs.next();

            }
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }
    public  static  void  exit() throws InterruptedException{
        System.out.println("Exiting System.");
        int i=5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using the Reservation System");

    }

    public static void main(String[] args) {
        try{
            Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_db","root","kishori15");
            System.out.println("connection establish");
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner sc= new Scanner(System.in);
                System.out.println("1.Reserve the Room ");
                System.out.println("2.View Reservation");
                System.out.println("3.Get Room Number");
                System.out.println("4.Update Reservation");
                System.out.println("5.Delete Reservation ");
                System.out.println("0. Exit");

                System.out.println("Choose an Option:");
                int choice =sc.nextInt();
                switch(choice){
                    case 1:
                        reserveRoom(connection,sc);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection,sc);
                        break;
                    case 4:
                        updateReservation(connection,sc);
                        break;
                    case 5:
                        deleteReservation(connection,sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please Enter Valid option");


                }
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

}