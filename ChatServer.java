import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
public class ChatServer implements ActionListener {
    JFrame frame =  new JFrame();
    JPanel p1;
    JLabel label, title, status;
    JTextField textField;
    JButton button;
    static JPanel panel;
    static ServerSocket serverSocket;
    static Socket socket;
    static DataInputStream din;
    static DataOutputStream dout;
    boolean isTyping = false;
    static Box vertical = Box.createVerticalBox();

    public void initializeComponents() {
       p1 = new JPanel();
       p1.setLayout(null);
       p1.setBounds(0,0,500,60);
       p1.setBackground(new Color(7,94,84));
       frame.add(p1);



       ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/backarrow.png"));
       Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_FAST);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l1 = new JLabel(i3);        
        l1.setBounds(10, 15, 25,25);
        l1.setLayout(null);
        l1.setVisible(true);
    
        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/options.png"));
        Image i5 = i4.getImage().getScaledInstance(15, 30, Image.SCALE_FAST);
         ImageIcon i6 = new ImageIcon(i5);
         JLabel l2 = new JLabel(i6);        
         l2.setBounds(440, 15, 15,30);
         l2.setLayout(null);
         l2.setVisible(true);

         ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
         Image i8 = i7.getImage().getScaledInstance(40, 40, Image.SCALE_FAST);
          ImageIcon i9 = new ImageIcon(i8);
          JLabel l3 = new JLabel(i9);        
          l3.setBounds(370, 10, 40,40);
          l3.setLayout(null);
          l3.setVisible(true);

          ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
         Image i11 = i10.getImage().getScaledInstance(40, 40, Image.SCALE_FAST);
          ImageIcon i12= new ImageIcon(i11);
          JLabel l4 = new JLabel(i12);        
          l4.setBounds(300, 10, 40,40);
          l4.setLayout(null);
          l4.setVisible(true);

      l1.addMouseListener(new MouseAdapter() {
          public void mouseClicked(MouseEvent me) {
            System.exit(0);
          }
      });

      p1.add(l1);
      p1.add(l2);
      p1.add(l3);
      p1.add(l4);

       title = new JLabel("Ninad");
       title.setBounds(60,8,100,20);
       title.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
       title.setForeground(Color.WHITE);
       p1.add(title);

       status = new JLabel("Active now");
       status.setBounds(60,28,100,20);
       status.setFont(new Font("SAN_SERIF", Font.PLAIN, 15));
       status.setForeground(Color.WHITE);
       p1.add(status);
        
       panel = new JPanel();
       panel.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
       panel.setVisible(true);
       panel.setLayout(null);
       panel.setBackground(Color.WHITE);

       JScrollPane scrollPane = new JScrollPane(panel);
      scrollPane.setBounds(5,70,475,520);
      scrollPane.setBorder(BorderFactory.createEmptyBorder());
      frame.add(scrollPane);

       button = new JButton("Send");
       button.setBounds(375, 600, 100, 50);
       button.setBackground(new Color(7,94,84));
       button.setForeground(Color.WHITE);
       button.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
       button.addActionListener(this);
       frame.add(button);

       Timer timer = new Timer(1, new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
            if (!isTyping)
                status.setText("Active now");
        }
    });

    timer.setInitialDelay(500);

    textField = new JTextField();
    textField.setBounds(10, 600, 360, 50);
    textField.setFont(new Font("SAN_SERIF", Font.PLAIN, 18));

    textField.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent ke) {
            status.setText("typing...");
            timer.stop();
            isTyping = true;
        }

        @Override
        public void keyReleased(KeyEvent ke) {
            isTyping = false;
            if (!timer.isRunning())
                timer.start();
        }
      });
      frame.add(textField);

      frame.getContentPane().setBackground(Color.WHITE);
    }    

    public void actionPerformed(ActionEvent event) {
       
        try {
            String output = textField.getText();
            JLabel lbl = formatLabel(output);
            //panel.add(lbl);
            panel.revalidate();
            panel.repaint();
        
            panel.setLayout(new BorderLayout());
            JPanel right = new JPanel(new BorderLayout());
            right.setBackground(Color.WHITE);
            right.add(lbl, BorderLayout.LINE_END);
            vertical.setBackground(Color.WHITE);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));
            panel.add(vertical, BorderLayout.PAGE_START);

            panel.setVisible(true);
            dout.writeUTF(output);
            
        } catch(Exception e) {
            System.out.println("Error");
        } 
        textField.setText("");  
    }

    public static JLabel formatLabel(String op) {
        JLabel l5 = new JLabel(op);
              
        l5.setBackground(new Color(37,211,102));
        l5.setBorder(new EmptyBorder(15,15,15,50) );
        l5.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        l5.setOpaque(true);
        l5.revalidate();
        l5.repaint();
        l5.setVisible(true);
        return l5;
    }

    
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.frame.setSize(500,700);
    
        server.frame.setLayout(null);
        server.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        server.initializeComponents();
        server.frame.setUndecorated(false);
        server.frame.setVisible(true);

        String message = "";
        try {
            
            serverSocket = new ServerSocket(7777);
            socket = serverSocket.accept();
            while(true) {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            message = din.readUTF();
            //panel.setText(panel.getText() + "\n" + message);
            JLabel lbl1 = formatLabel(message);

            JPanel left = new JPanel(new BorderLayout());
            left.add(lbl1, BorderLayout.LINE_START);
            left.setBackground(Color.WHITE);

            vertical.add(left);
            server.frame.validate();
            }

        } catch (Exception e) {
            
        }

    }
}
