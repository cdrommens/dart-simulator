package be.rommens.darts.simulator;

import static java.lang.Math.toDegrees;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import org.apache.commons.geometry.euclidean.twod.PolarCoordinates;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.chart.ui.Align;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("polar")
@SpringBootTest
public class PolarCoordinatesServiceTest {

    private static final int[] SEGMENT_VALUES = { 20, 1, 18, 4, 13, 6, 10, 15, 2, 17, 3, 19, 7, 16, 8, 11, 14, 9, 12, 5, 20 };

    @Autowired
    PolarCoordinatesService polarCoordinatesService;

    @Test
    void getPolarCoordinates() throws IOException {
        polarCoordinatesService.calculate();
    }

    @Test
    void calculateGrid() {
        int i = 0;
        System.out.println("field;single;treble;double");
        for (int x = 0; x < 351; x = x + 18) {
            var single = PolarCoordinates.of(134.5, Math.toRadians(x)).toCartesian();
            var treble = PolarCoordinates.of(103, Math.toRadians(x)).toCartesian();
            var doubles = PolarCoordinates.of(166, Math.toRadians(x)).toCartesian();
            System.out.printf("%s;%s,%s;%s,%s;%s,%s\n", SEGMENT_VALUES[i],
                    new BigDecimal(String.valueOf(single.getX())).setScale(0, RoundingMode.HALF_UP),
                    new BigDecimal(String.valueOf(single.getY())).setScale(0, RoundingMode.HALF_UP),
                    new BigDecimal(String.valueOf(treble.getX())).setScale(0, RoundingMode.HALF_UP),
                    new BigDecimal(String.valueOf(treble.getY())).setScale(0, RoundingMode.HALF_UP),
                    new BigDecimal(String.valueOf(doubles.getX())).setScale(0, RoundingMode.HALF_UP),
                    new BigDecimal(String.valueOf(doubles.getY())).setScale(0, RoundingMode.HALF_UP));
            i++;
        }
        var single = PolarCoordinates.of(11.35, Math.toRadians(0)).toCartesian();
        System.out.printf("%s;%s,%s;%s\n", 25,
                new BigDecimal(String.valueOf(single.getX())).setScale(0, RoundingMode.HALF_UP),
                new BigDecimal(String.valueOf(single.getY())).setScale(0, RoundingMode.HALF_UP),
                "0,0");
    }

    @Test
    void iets() {
        var r = Vector2D.of(
                ThreadLocalRandom.current().nextGaussian(42, 5),
                ThreadLocalRandom.current().nextGaussian(128, 5));

        PolarCoordinates result = PolarCoordinates.fromCartesian(Vector2D.of(128, 42));
        int index = Math.floorDiv(new BigDecimal(String.valueOf(Math.toDegrees(result.getAzimuth()))).setScale(0, RoundingMode.HALF_UP).intValue() + 9, 18);
        int number = SEGMENT_VALUES[index];

        System.out.println(result.getRadius());
        System.out.println(result.getAzimuth());
        System.out.println(number);

        PolarCoordinates result2 = PolarCoordinates.fromCartesian(r);
        int index2 = Math.floorDiv(new BigDecimal(String.valueOf(Math.toDegrees(result2.getAzimuth()))).setScale(0, RoundingMode.HALF_UP).intValue() + 9, 18);
        int number2 = SEGMENT_VALUES[index2];

        System.out.println(r);
        System.out.println(result2.getRadius());
        System.out.println(result2.getAzimuth());
        System.out.println(number2);

        var rechts = PolarCoordinates.of(103, Math.toRadians(9)).toCartesian();
        var links = PolarCoordinates.of(103, Math.toRadians(351)).toCartesian();
        var boven = PolarCoordinates.of(107, Math.toRadians(0)).toCartesian();
        var onder = PolarCoordinates.of(99, Math.toRadians(0)).toCartesian();
        System.out.println(rechts);
        System.out.println(links);
        System.out.println(boven);
        System.out.println(onder);
    }

    @Test
    void check() throws IOException {
        /*
        stel: center T20 : 103, 0
        rechts: 102, 16
        links: 102, -16
        boven : 107, 0
        onder: 99, 0

        voor x : ik wil dat 42% van de values tussen 99 en 107 zit : nextGaussian() * 103 + (107-99=8)
        voor y : ik wil dat 42% van de values tussen 16 en -16 zit : nextGaussian() * 0 + (32)

        https://www.stapplet.com/normal.html
         */
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Boys");
        List<Integer> results = new ArrayList<>();
        for (int i=0; i < 9; i++) {
            //double x = ThreadLocalRandom.current().nextGaussian(103, 7.2);
            double x = ThreadLocalRandom.current().nextGaussian(103, 7);
            //double y = ThreadLocalRandom.current().nextGaussian(0, 28.7);
            double y = ThreadLocalRandom.current().nextGaussian(0, 15);
            PolarCoordinates result2 = PolarCoordinates.fromCartesian(Vector2D.of(x, y));
            int index2 = Math.floorDiv(new BigDecimal(String.valueOf(Math.toDegrees(result2.getAzimuth()))).setScale(0, RoundingMode.HALF_UP).intValue() + 9, 18);
            if (result2.getRadius() > 99 && result2.getRadius() <= 107) {
                results.add(SEGMENT_VALUES[index2] * 3);
            } else {
                results.add(SEGMENT_VALUES[index2]);
            }
            series1.add(toDegrees(result2.getAzimuth()), result2.getRadius());
        }
        var map = results.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        map.forEach((key, value) -> System.out.println(key + "=" + value));
        System.out.println(results.stream().reduce(0, Integer::sum)/9*3);

        dataset.addSeries(series1);

        ValueAxis radiusAxis = new NumberAxis();
        radiusAxis.setUpperBound(226);
        radiusAxis.setTickLabelsVisible(false);
        radiusAxis.setAxisLineVisible(false);
        radiusAxis.setTickMarksVisible(false);
        DefaultPolarItemRenderer renderer = new DefaultPolarItemRenderer();
        renderer.setShapesVisible(true);
        Stroke dashedStroke = new BasicStroke(
                0.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                0.0f, new float[] {0.0f, 1e10f}, 1.0f );
        renderer.setSeriesStroke(0, dashedStroke);

        ImageIcon icon = new ImageIcon(PolarCoordinatesService.class.getClassLoader().getResource("img.png"));

        PolarPlot plot = new PolarPlot(dataset, radiusAxis, renderer);
        plot.setCounterClockwise(false);
        plot.setBackgroundImage(icon.getImage());
        plot.setBackgroundImageAlignment(Align.FIT);
        //plot.setBackgroundImageAlpha(1.0f);
        plot.setMargin(0);
        plot.setInsets(RectangleInsets.ZERO_INSETS);
        plot.setBackgroundPaint(new Color(255,255,255,0));

        JFreeChart chart = new JFreeChart(null, plot);
        chart.setBackgroundPaint(new Color(255,255,255,0));
        chart.removeLegend();

        File file = new File("./out.png");
        ChartUtils.saveChartAsPNG(file, chart, 1000,1000);
    }


    @Test
    void checkRadius() throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Boys");
        List<Integer> results = new ArrayList<>();

        /*
        for (int i=0; i < 9; i++) {
            //double x = ThreadLocalRandom.current().nextGaussian(103, 7.2);
            double x = ThreadLocalRandom.current().nextGaussian(103, 7);
            //double y = ThreadLocalRandom.current().nextGaussian(0, 28.7);
            double y = ThreadLocalRandom.current().nextGaussian(0, 15);
            PolarCoordinates result2 = PolarCoordinates.fromCartesian(Vector2D.of(x, y));
            int index2 = Math.floorDiv(new BigDecimal(String.valueOf(Math.toDegrees(result2.getAzimuth()))).setScale(0, RoundingMode.HALF_UP).intValue() + 9, 18);
            if (result2.getRadius() > 99 && result2.getRadius() <= 107) {
                results.add(SEGMENT_VALUES[index2] * 3);
            } else {
                results.add(SEGMENT_VALUES[index2]);
            }
            series1.add(toDegrees(result2.getAzimuth()), result2.getRadius());
        }
         */

        for (int degrees = 0; degrees < 360; degrees = degrees + 10) {
            PolarCoordinates polar = PolarCoordinates.of(32, Math.toRadians(degrees));
            series1.add(toDegrees(polar.getAzimuth()), polar.getRadius());
        }

        /*
                (xp - xc)^2 + (yp -yc)^2 < r^2

                xp,yp => punt ; xc, yc => center cirkel ; r= radius cirkel
         */

        PolarCoordinates center = PolarCoordinates.of(103, Math.toRadians(90));
        series1.add(toDegrees(center.getAzimuth()), center.getRadius());
        PolarCoordinates punt = PolarCoordinates.of(155, Math.toRadians(95));
        series1.add(toDegrees(punt.getAzimuth()), punt.getRadius());

        var center2d = center.toCartesian();
        var punt2d = punt.toCartesian();
        var rek = ((punt2d.getX() - center2d.getX())*(punt2d.getX() - center2d.getX())) + ((punt2d.getY() -center2d.getY())*(punt2d.getY() -center2d.getY()));
        if (rek < (32*32)) {
            System.out.println("in");
        } else {
            System.out.println("out");
        }



        dataset.addSeries(series1);





        ValueAxis radiusAxis = new NumberAxis();
        radiusAxis.setUpperBound(226);
        radiusAxis.setTickLabelsVisible(false);
        radiusAxis.setAxisLineVisible(false);
        radiusAxis.setTickMarksVisible(false);
        DefaultPolarItemRenderer renderer = new DefaultPolarItemRenderer();
        renderer.setShapesVisible(true);
        Stroke dashedStroke = new BasicStroke(
                0.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                0.0f, new float[] {0.0f, 1e10f}, 1.0f );
        renderer.setSeriesStroke(0, dashedStroke);

        ImageIcon icon = new ImageIcon(PolarCoordinatesService.class.getClassLoader().getResource("img.png"));

        PolarPlot plot = new PolarPlot(dataset, radiusAxis, renderer);
        plot.setCounterClockwise(false);
        plot.setBackgroundImage(icon.getImage());
        plot.setBackgroundImageAlignment(Align.FIT);
        //plot.setBackgroundImageAlpha(1.0f);
        plot.setMargin(0);
        plot.setInsets(RectangleInsets.ZERO_INSETS);
        plot.setBackgroundPaint(new Color(255,255,255,0));

        JFreeChart chart = new JFreeChart(null, plot);
        chart.setBackgroundPaint(new Color(255,255,255,0));
        chart.removeLegend();

        File file = new File("./out.png");
        ChartUtils.saveChartAsPNG(file, chart, 1000,1000);
    }

}
