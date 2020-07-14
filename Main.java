import java.io.*;
import java.util.*;
class Date implements Comparable<Date>{
    private int year;
    private int month;
    private int date;
    private int hour;
    private int min;
    private int sec;
    /* Assuming mini second can be very long*/
    private long minisec;
    Date(String s)
    {
        String[] components=s.split("[-:T.Z\\s+]");
        year=Integer.parseInt(components[0]);
        month=Integer.parseInt(components[1]);
        date=Integer.parseInt(components[2]);
        hour=Integer.parseInt(components[3]);
        min=Integer.parseInt(components[4]);
        sec=Integer.parseInt(components[5]);

        /* 6th component will be empty string */
        minisec=Long.parseLong(components[7]);
    }
    public int compareTo(Date d)
    {
        if(this.year!=d.year)
        return this.year-d.year;
        if(this.month!=d.month)
        return this.month-d.month;
        if(this.date!=d.date)
        return this.date-d.date;
        if(this.hour!=d.hour)
        return this.hour-d.hour;
        if(this.min!=d.min)
        return this.min-d.min;
        if(this.sec!=d.sec)
        return this.sec-d.sec;
        else if(this.minisec<d.minisec)
        return -1;
        else if(this.minisec>d.minisec)
        return 1;
        return 0;
    }
    public void print()
    {
        System.out.println(year+" "+month+" "+date+" "+hour+" "+min+" "+sec+" "+minisec+" ");
    }

}

class Solver{
    static long temp;
    public static void solve(File[] files,Date from, Date end) throws Exception
    {
        
        int k=findIndex(files,from);
        if(k==-1)
        return ;
        System.out.println(k);
        RandomAccessFile f=new RandomAccessFile(files[k], "r");    
        long l=1;
        long r=f.length();
        long found=-1;
        while(l<=r)
        {
            long mid=(l+r)/2L;
            temp=-1;
            Date dt=give(f,mid,r);
            if(dt==null)
                r=mid-1L;
            else if(dt.compareTo(from)>=0)
            {r=mid-1L;found=temp;}
            else
            l=mid+1L;
        }
        if(found!=-1){
            boolean res=true;
            PrintWriter pw=new PrintWriter(System.out);
            for(int i=k;i<files.length;i++)
            {
                f=new RandomAccessFile(files[k], "r");
                f.seek(found);
                String hold;
                while((hold=f.readLine())!=null)
                {
                    if(new Date(hold).compareTo(end)>0)
                    {
                        res=false;
                        break;
                    }
                    pw.println(hold);
                }
                f.close();
                if(!res)
                break;
                found=1;
            }
            pw.flush();
            pw.close();
        }
        f.close();
    }
    public static int findIndex(File[] files,Date from) throws Exception
    {
        int l=0;
        int r=files.length-1;
        int found=-1;
        BufferedReader br;
        while(l<=r)
        {
            int mid=(l+r)/2;
            br=new BufferedReader(new FileReader(files[mid]));
            Date en=new Date(br.readLine());
            if(en.compareTo(from)<=0)
            {
                found=mid;
                l=mid+1;
            }
            else
            r=mid-1;
            br.close();
        }
        return found;
    }
    public static Date give(RandomAccessFile f,long pos,long end) throws Exception
    {
        f.seek(pos);
        f.readLine();
        temp=f.getFilePointer();
        if(temp>=end)
        return null;
        return new Date(f.readLine());
    }
    public static long time()
    {
        return System.currentTimeMillis();
    }
    public static void see(File[] files,int p) throws Exception
    {
        for(int i=0;i<files.length;i++)
        {
            BufferedReader br=new BufferedReader(new FileReader(files[i]));
            for(int j=0;j<p;j++)
            {
                String s=br.readLine();
                System.out.println(s);
            }
            br.close();
            System.out.println(i+"   -----END-------"+" "+files[i].length());
        }
    }
}
public class Main {
    public static void main(String args[]) throws Exception
    {
        if(args.length==0)
        return;
        File f=new File(args[0]);
        System.out.println(f.isDirectory()+" "+f.length());
        String[] list=new File(args[0]).list();
        File[] files=new File[list.length];
        for(int i=0;i<list.length;i++)
        files[i]=new File(args[0]+"\\"+list[i]);
        Date start=new Date(args[1]);
        Date end=new Date(args[2]);
         Solver.solve(files,start,end);
    }
}

/*
2020-07-13T16:53:56Z.1504
2020-07-13T16:53:58Z.1502
*/
