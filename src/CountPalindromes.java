import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.math.BigInteger;
import parcs.*;

public class CountPalindromes implements AM
{
    private static long startTime = 0;

    public static int countPalindromes(String s)
    {
        int pos_f = 0;
        int pos_s = 0;
        for (int j=0; j<s.length(); j+=1)
            {
                if (s.charAt(j) == ' ' && pos_f == 0)
                {
                    pos_f = j;
                } else 
                if (s.charAt(j) == ' ')
                {
                    pos_s = j;
                }
            }
        int start = Integer.valueOf(s.substring(0, pos_f));
        int step = Integer.valueOf(s.substring(pos_f+1, pos_s));
        s = s.substring(pos_s+1, s.length());
        System.err.println("Calculating (in function) for .. "+start+" "+step+" "+s);
        int n = s.length();
        int res = 0;
        int l = start;
        while (l<n)
        {
            int j = 0;
            while (l - j >= 0 && l + j < n && s.charAt(l - j) == s.charAt(l + j))
            {
                res += 1;
                j += 1;
            }
            j = 0;
            while (l - j >= 0 && l + j + 1 < n && s.charAt(l - j) == s.charAt(l + j + 1))
            {
                res += 1;
                j += 1;
            }
            l += step;
        }
        System.err.println("Result (in function) is " + res);
        return res;
    }


    public static void main(String[] args) throws Exception
    {
        System.err.println("Preparing...");
        if (args.length != 1) {
            System.err.println("Number of workers not specified");
            System.exit(1);
        }
        int numberWorkers = Integer.parseInt(args[0]);
        task curtask = new task();
        curtask.addJarFile("CountPalindromes.jar");
        AMInfo info = new AMInfo(curtask, null);
        System.err.println("Reading input...");
        String S = "";
        try
        {
            Scanner sc = new Scanner(new File(info.curtask.findFile("example_small.txt")));
            S = sc.nextLine();
        }
        catch (IOException e) {e.printStackTrace(); return;}
        int len = S.length();
        System.err.println("Forwarding parts to workers...");
        startTime = System.nanoTime();
        channel[] channels = new channel[numberWorkers];
        for (int i = 0; i < numberWorkers; i++)
        {
            point p = info.createPoint();
            channel c = p.createChannel();
            p.execute("CountPalindromes");
            // c.write(i);
            // c.write(numberWorkers);
            String s = Integer.toString(i)+" "+Integer.toString(numberWorkers)+" "+S;
            c.write(s);
            channels[i] = c;
        }
        System.err.println("Getting results");
        int[] results = new int[numberWorkers];
        for (int i = 0; i < numberWorkers; i++)
        {
            results[i] = channels[i].readInt();
        }
        System.err.println("Calculation of the result");
        int res = 0;
        for (int i = 0; i < numberWorkers; i++)
        {
            res += results[i];
        }
        long endTime = System.nanoTime();
        System.out.println("Result: " + res);
        long timeElapsed = endTime - startTime;
        double seconds = timeElapsed / 1_000_000_000.0;
        System.err.println("Time passed: " + seconds + " seconds.");
        curtask.end();
    }

    public void run(AMInfo info)
    {
        //int start_pos = (int)info.parent.readObject();
        //int step = (int)info.parent.readObject();
        String s = (String)info.parent.readObject();
        System.err.println("Started run with " + s + " string");
        int subresult = countPalindromes(s);
        System.err.println("Result in run is " + subresult);
        info.parent.write(subresult);
    }
}
