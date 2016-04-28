package com.onroute.database.resource;

import com.onroute.database.enums.neo4j.Labels;
import com.onroute.database.enums.TabletActions;
import com.onroute.database.models.PassengerModel;
import com.onroute.database.models.SessionModel;
import com.onroute.database.resource.base.BaseResource;
import com.onroute.database.websocket.Response;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Transaction;

import javax.inject.Inject;


/**
 * This resource intereacts with a Passenger.
 */
public class PassengerResource extends BaseResource {
    @Inject MediaResource mediaResource;


    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "passenger";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Response handleRequest(String function, SessionModel session, String data) throws Exception {
        PassengerModel passenger = session.getPassenger();
        switch (function) {
            case "activity":
                break;

            case "ask_otp":
                passenger = ask_otp(passenger);
                break;

            case "verify_otp":
            case "skip_otp":
                passenger = getByPhonenumber(passenger);

                // Add the update movie list command
                session.addCommand(TabletActions.UPDATE_MOVIE_LIST,
                        gson.toJson(mediaResource.getRecommendedMovies(session)));
                break;
            case "checkin":
                passenger = getByMac(gson.fromJson(data, String.class));

                // Add the update movie list command
//                String json =
                session.addCommand(TabletActions.UPDATE_MOVIE_LIST,
                        gson.toJson(mediaResource.getRecommendedMovies(session)));
                System.out.println("hit1");
                break;

            case "get_advertisement":
                break;

            case "get_content":
                break;

            case "save":
                passenger = save(passenger);
                break;

            default:
                throw new NotFoundException("no function found");
        }

        session.setPassenger(passenger);
        return Response.returnSuccess(session);
    }


    /**
     * This function is called when the user is to be verified by a OTP.
     * <p/>
     * The function returns the passenger, without verifying the OTP. This means anyone can get details of a passenger
     * if he/she accesses this function directly. But for convinence, we'll disregard security, because the tablet can
     * instantaneously sign-in a user without having to wait for the server to verify & fetch the user from the DB.
     *
     * @param passenger The passenger that with the given phonenumber
     * @return {@link PassengerModel} representing the passenger from the DB.
     */
    PassengerModel ask_otp(PassengerModel passenger) {
        int otp = (int) Math.floor(Math.random() * 1000);

        // Set and send the OTP
        System.out.println("sending OTP " + otp);

        try (Transaction tx = graphDb.beginTx()) {
            // Get the passenger from the DB
            Node passengerNode = passenger.getNode(graphDb);

            // If the passenger exists, in the DB then we return it to the tablet.
            if (passengerNode != null) passenger.clonePropertiesFromNode(passengerNode);
        }

        return passenger;
    }


    PassengerModel getByMac(String mac) {
        PassengerModel passenger = new PassengerModel();
        passenger.setMacAddress(mac);

        try (Transaction tx = graphDb.beginTx()) {
            Node passengerNode = passenger.getNode(graphDb);

            // Create the node if it doesn't exist
            if (passengerNode == null) {
                passengerNode = graphDb.createNode(Labels.Passenger);
                passenger.clonePropertiesToNode(passengerNode);
            }

            // Now get all the properties from the node, into our model
            passenger.clonePropertiesFromNode(passengerNode);
            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return passenger;
    }


    PassengerModel save(PassengerModel passenger) {
        try (Transaction tx = graphDb.beginTx()) {
            Node passengerNode = passenger.getNode(graphDb);

            // Create the node if it doesn't exist
            if (passengerNode == null) {
                passengerNode = graphDb.createNode(Labels.Passenger);
            }

            // Save the properties and close the transaction
            passenger.clonePropertiesToNode(passengerNode);
            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return passenger;
    }


    private PassengerModel getByPhonenumber(PassengerModel passenger) {
        try (Transaction tx = graphDb.beginTx()) {
            Node passengerNode = passenger.getNode(graphDb);

            // Create the node if it doesn't exist
            if (passengerNode == null) {
                passengerNode = graphDb.createNode(Labels.Passenger);
                passenger.clonePropertiesToNode(passengerNode);
            }

            // Now get all the properties from the node, into our model
            passenger.clonePropertiesFromNode(passengerNode);
            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return passenger;
    }
}