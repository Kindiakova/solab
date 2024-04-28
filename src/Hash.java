import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.math.BigInteger;
import parcs.*;

public class Hash implements AM
{
    private static long startTime = 0;

    public static int countPalindromes(String s)
    {
        int res = 1;
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
            System.out.println("Result[i]: " + results[i]);
            
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
        
        int subresult = 1;
        info.parent.write(subresult);
    }
}
