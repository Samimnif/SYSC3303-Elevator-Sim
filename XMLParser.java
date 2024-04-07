import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class XMLParser extends DefaultHandler {
    private ArrayList<Elevator> elevator;
    private StringBuilder elementContent;

    @Override
    public void startDocument() {
        elevator = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("Elevator")) {
            int i = 0;
            Elevator current_Elevator = new Elevator(0, 0,5);
            elevator.add(current_Elevator);
        }
        elementContent = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        elementContent.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("id")) {
            elevator.get(elevator.size() - 1).setId(Integer.parseInt(elementContent.toString()));
            /*
        }else if(qName.equalsIgnoreCase("currentJo")){
            elevator.get(elevator.size()-1).setName(elementContent.toString());
        }else if(qName.equalsIgnoreCase("department")){
            elevator.get(elevator.size()-1).setDepartment(elementContent.toString());

             */
        } else if (qName.equalsIgnoreCase("destinationFloor")) {
            int destinationFloor = Integer.parseInt(elementContent.toString());
            elevator.get(elevator.size() - 1).getCurrentJob().setDestinationFloor(destinationFloor);
        } else if (qName.equalsIgnoreCase("timeStamp")) {
            String timeStamp = elementContent.toString();
            elevator.get(elevator.size() - 1).getCurrentJob().setTimeStamp(timeStamp);
        } else if (qName.equalsIgnoreCase("button")) {
            String button = elementContent.toString();
            elevator.get(elevator.size() - 1).getCurrentJob().setButton(button);
        } else if (qName.equalsIgnoreCase("pickUpFloor")) {
            int pickUpFloor = Integer.parseInt(elementContent.toString());
            elevator.get(elevator.size() - 1).getCurrentJob().setPickupFloor(pickUpFloor);
            /*
        }else if(qName.equalsIgnoreCase("currentState")) {
           currentState = Integer.parseInt(elementContent.toString());
          elevator.get(elevator.size()-1).getCurrentState().setCurrentState(currentState);
        }

             */
        }
    }

        public ArrayList<Elevator> readXMLEmployeeFile() throws IOException {
            String fileName = null;
            try {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser parser = spf.newSAXParser();
                File file = new File(fileName);
                parser.parse(file, this);
                return elevator;
            } catch (IOException | ParserConfigurationException | SAXException e) {
                throw new IOException(e);
            }
        }
    }



