package app.services;

public class SVG
{
    private static final String SVG_TEMPLATE = "<svg version=\"1.1\"\n" +
            "     x=\"%d\" y=\"%d\"\n" +
            "     viewBox=\"%s\"  width=\"%s\" \n" +
            "     preserveAspectRatio=\"xMinYMin\">";

    private static final String SVG_ARROW_DEFS = "<defs>\n" +
            "        <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
            "            <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" +
            "        <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
            "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
            "        </marker>\n" +
            "    </defs>";

    private static final String SVG_RECT_TEMPLATE = "<rect x=\"%.2f\" y=\"%.2f\" width=\"%.1f\" height=\"%.1f\" style=\"%s\"  transform=\"translate(%f,%f)\" stroke-dasharray=\"%f %f\" />";
    
    private static final String SVG_LINE_TEMPLATE = "<line x1=\"%.2f\" y1=\"%.2f\" x2=\"%.1f\" y2=\"%.1f\" style=\"%s\" />";
    
    private static final String SVG_TEXT_TEMPLATE = "<text style=\"text-anchor: middle\" transform=\"translate(%f, %f) rotate(%f)\">%s</text>";

    private StringBuilder svg = new StringBuilder();

    public SVG(int x, int y, String viewBox, String width) {

        svg.append(String.format(SVG_TEMPLATE, x, y, viewBox, width));
        svg.append(SVG_ARROW_DEFS);
    }

    public void addRectangle(double x, double y, double height, double width, String style, double translateX, double translateY) {
        svg.append(String.format(SVG_RECT_TEMPLATE, x, y, height, width, style, translateX, translateY, 0d, 0d));
    }
    
    public void addRectangle(double x, double y, double height, double width, String style) {
        svg.append(String.format(SVG_RECT_TEMPLATE, x, y, height, width, style, 0d, 0d, 0d, 0d));
    }    
    public void addRectangle(double x, double y, double height, double width, double translateX, double translateY) {
        svg.append(String.format(SVG_RECT_TEMPLATE, x, y, height, width, "stroke: #000000; fill:none;", translateX, translateY, 0d, 0d));
    }
    
    public void addRectangle(double x, double y, double height, double width) {
        svg.append(String.format(SVG_RECT_TEMPLATE, x, y, height, width, "stroke: #000000; fill:none;", 0d, 0d, 0d, 0d));
    }
    public void addRectangleWithDashedBorder(double x, double y, double height, double width) {
        svg.append(String.format(SVG_RECT_TEMPLATE, x, y, height, width, "stroke: #000000; fill:none; stroke-width:5;", 0d, 0d, 5d, 10d));
    }

    public void addLine(double x1, double y1, double x2, double y2, String style){
        svg.append(String.format(SVG_LINE_TEMPLATE, x1, y1, x2, y2, style));
    }

    public void addArrow(double x1, double y1, double x2, double y2, String style){
        style += "marker-start: url(#beginArrow);";
        style += "marker-end: url(#endArrow);";
        addLine(x1, y1, x2, y2, style);
    }

    public void addText(float f, float g, float h, String text){
        svg.append(String.format(SVG_TEXT_TEMPLATE, f, g, h, text));
    }

    public void addSvg(SVG innerSvg){
        svg.append(innerSvg.toString());
    }

    @Override
    public String toString(){
        return svg.append("</svg>").toString();
    }
}