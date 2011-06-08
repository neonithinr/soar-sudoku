package alit;

import sml.*;

import java.util.HashSet;
import java.util.Set;

/**
 * User: Alexander Litvinenko
 * Date: 6/8/11
 * Time: 12:35 PM
 */
public class Proba {

        private static final String PATH = "sudoku.soar";
//    private static final String PATH = "helloworld.soar";
    private Kernel kernel;
    private Agent agent;

    public Proba() {
        int[] constraints = new int[]{1, 2, 3};
        System.out.println("Creating kernel...");
        kernel = Kernel.CreateKernelInNewThread();
        System.out.println("Creating agent..");
        agent = kernel.CreateAgent("soar");
        System.out.println("Loading productions...");
        boolean b = agent.LoadProductions(PATH);

        System.out.println("Creating input link...");
//        for(int constraint : constraints) {
//            agent.GetInputLink().CreateIntWME("constraint", constraint);
        agent.GetInputLink().CreateIntWME("constraint", 1);
//        }
        agent.GetInputLink().CreateStringWME("hello", "world");
        System.out.println("Registering for update event...");
        agent.Commit();
        agent.RunSelfTilOutput();
//        System.out.println("----------------: " + agent.GetOutputLink().GetChild(0).GetValueAsString());
        for (int index = 0; index < agent.GetNumberCommands(); ++index) {
            Identifier command = agent.GetCommand(index);

            String name = command.GetCommandName();

            System.out.println("Received command: " + name);
            kernel.StopAllAgents();
            command.AddStatusComplete();
        }


        agent.ClearOutputLinkChanges();

        System.out.println("Update event complete.");


//        kernel.RegisterForUpdateEvent(
//                smlUpdateEventId.smlEVENT_AFTER_ALL_OUTPUT_PHASES,
//                new Kernel.UpdateEventInterface()
//                {
//                    public void updateEventHandler(int eventID, Object data,
//                            Kernel kernel, int runFlags)
//                    {
//                        System.out.println("Update event...");
//
//                        for (int index = 0; index < agent.GetNumberCommands(); ++index)
//                        {
//                            Identifier command = agent.GetCommand(index);
//
//                            String name = command.GetCommandName();
//
//                            System.out.println("Received command: " + name);
//                            kernel.StopAllAgents();
//                            command.AddStatusComplete();
//                        }
//
//
//                        agent.ClearOutputLinkChanges();
//
//                        System.out.println("Update event complete.");
//                    }
//                }, null);
        System.out.println("Running agents...");
        kernel.RunAllAgents(5);
        System.out.println("Shutting down...");
        kernel.DestroyAgent(agent);
        agent = null;
        kernel.Shutdown();
        kernel = null;

        System.out.println("Done.");
    }

    public static void main(String[] args) {
        new Proba();
    }
}
