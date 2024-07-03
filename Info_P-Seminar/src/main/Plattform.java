package main;

// Hier war ein überflüssiges Import-Statement
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Plattform
{
    private int x, y;
    private int plattformLaenge, plattformHoehe;

    //Änderung in ein int, String Plattformtyp unnötig, da Array
    private int schaedlich = 0;

    //Nötige Änderung in ein Arrray
    private BufferedImage[] bild;

    //Diese Arraylist wird benötigt, um die Grafik der Plattform in der Klasse main.World einfach darstellen zu können; in ihr werden x- und y-Koordinaten der einzelnen Plattformteile gespeichert
    ArrayList<int[]> positions = new ArrayList<>();
    protected boolean alreadyInitialized = false;

    public Plattform()
    {

    }

    //Anpassen der Werte in der Klammer, Gründe siehe oben, Einheit für plattformlaenge und hoehe in Blöcken
    public void erzeugePlattform(int x, int y, int plattformLaenge, int plattformHoehe, int schaedlich) throws IOException, NullPointerException
    {
        if(alreadyInitialized)
        {
            System.out.println("Was zum Fick, die gibts ja schon");
            return;
        }

        this.x = x;
        this.y = y;
        this.plattformLaenge = plattformLaenge;
        this.plattformHoehe = plattformHoehe;
        this.schaedlich = schaedlich;

        int zeiger = 4;

        bild = new BufferedImage[plattformHoehe*plattformLaenge];

        int platformImageSize = ImageIO.read(new File("res/Brick_eckeOL1.png")).getHeight();

        //erstellen der Ecken
        bild[0] = ImageIO.read(new File("res/Brick_eckeOL1.png"));
        positions.add(0, new int[]{x, y});
        bild[1] = ImageIO.read(new File("res/Brick_eckeOR1.png"));
        positions.add(1, new int[]{(plattformLaenge-1)*platformImageSize+x, y});
        bild[2] = ImageIO.read(new File("res/Brick_eckeUL1.png"));
        positions.add(2, new int[]{x, (plattformHoehe-1)*platformImageSize+y});
        bild[3] = ImageIO.read(new File("res/Brick_eckeUR1.png"));
        positions.add(3, new int[]{(plattformLaenge-1)*platformImageSize+x, (plattformHoehe-1)*platformImageSize+y});

        //Wie viele Plattformen müssen jeweils oben und unten erstellt werden?
        int zuErzeugendePlattformenObenUndUnten = plattformLaenge - 2;

        //erstellen der oberen und unteren Segmente
        for(int i=0; i<zuErzeugendePlattformenObenUndUnten; i++)
        {
            //Auslassen der Ecken
            bild[zeiger] = ImageIO.read(new File("res/Brick_oben1.png"));
            positions.add(zeiger, new int[]{(i+1)*platformImageSize+x, y});
            zeiger++;
            //Auslassen der Ecken + Auslassen der bisher erstelten Plattformen oben
            bild[zeiger] = ImageIO.read(new File("res/Brick_unten1.png"));
            positions.add(zeiger, new int[]{(i+1)*platformImageSize+x, y+(plattformHoehe-1)*platformImageSize});
            zeiger++;
        }

        //Wie viele Plattformen müssen jeweils links und rechts erstellt werden?
        int zuErzeugendePlattformenLinksUndRechts = plattformHoehe - 2;

        //erstellen der linken und rechten Segmente
        for(int i=0; i<zuErzeugendePlattformenLinksUndRechts; i++)
        {
            //Auslassen der Ecken und der Plattformteile oben und unten
            bild[zeiger] = ImageIO.read(new File("res/Brick_links1.png"));
            positions.add(zeiger, new int[]{x, (i+1)*platformImageSize+y});
            zeiger++;
            //-"- + Auslassen der bisher erstellten Plattformteile links
            bild[zeiger] = ImageIO.read(new File("res/Brick_rechts1.png"));
            positions.add(zeiger, new int[]{x+(plattformLaenge-1)*platformImageSize, (i+1)*platformImageSize+y});
            zeiger++;
        }

        //Wie viele Mittelteile müssen erstellt werden?
        int zuErzeugendeMittelStuecke = bild.length - 2*plattformHoehe - 2*(plattformLaenge-2);

        // erstellen der Mittelstücke
        for(int i=0;i<zuErzeugendeMittelStuecke;i++)
        {
            //Auslassen aller bisher erstellten Plattformstücke
            bild[zeiger] = ImageIO.read(new File("res/Brick_mitte1.png"));

            // In welcher Reihe muss das Teil sein?
            // Zuerst als float, dann wird geschaut, welche Reihe dazu passt
            // Plattformen mit einer Plattformhöhe von über 3 Blöcken funktionieren nicht! Warum?
            float currentRow = (float) i/zuErzeugendePlattformenObenUndUnten;
            int wtf = 0;
            for(int j=0;j<zuErzeugendePlattformenLinksUndRechts;j++)
            {
                if(j*zuErzeugendePlattformenObenUndUnten>=currentRow)
                {
                    wtf = j;
                    break;
                }
            }
            positions.add(zeiger, new int[]{(i+1)*platformImageSize+x, (wtf+1)*platformImageSize+y});
            zeiger++;
        }

        alreadyInitialized = true;
    }

    // Abfragen der Eigenschaft von Bild (für Methode main.World)
    public BufferedImage[] getImages() {

        return bild;

    }
    //Abfragen der Eigenschaft von schaedlich (für Methode main.World)
    public int getSchaedlich() {

        return schaedlich;

    }

    //Abfragen der x-Koordinate (für Methode main.World)
    public int getX(){
        return x;
    }

    //Abfragen der x-Koordinate (für Methode main.World)
    public int getY(){
        return y;
    }

    public int[] getDimensions()
    {
        return new int[]{plattformLaenge,plattformHoehe};
    }

    public ArrayList<int[]> getPositions()
    {
        return positions;
    }
}
