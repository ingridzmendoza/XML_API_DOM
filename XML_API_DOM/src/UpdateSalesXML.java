/*
Desarrollar un aplicación que utilice el API Document Object Model (DOM) para XML de Java para procesar el documento
sales.xml. El programa debe solicitar un porcentaje entre 5% y 15% y aplicar ese incremento al valor de las ventas del
departamento que el usuario especifique. Se debe generar un nuevo documento XML que se llame new_sales.xml.
 */
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.*;
import java.util.Scanner;

public class UpdateSalesXML {

    //metodo para validar pocentaje
    public static boolean validarPorcentaje(double porcentaje) {
        if (!(porcentaje >= 5 && porcentaje <= 15)) {
            System.err.println("El porcentaje ingresado no es válido, por favor ingrese un valor entre 5% y 15%.");
            return false;
        }
        return true;
    }

    //metodo para calcular venta con porcentaje aplicado
    public static double ventaGenerada(double porcentaje, double venta) {
        double nuevaVenta = venta * (1 + (porcentaje / 100));
        return nuevaVenta;
    }

    public static void main(String[] args) {
        try {
            // importar sales.xml
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("src\\sales.xml"));

            // solicitar al usuario los datos del porcentaje y el departamento
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese el departamento: ");
            String departamento = scanner.nextLine();

            System.out.print("Ingrese el porcentaje a incrementar (5% a 15%): ");
            double porcentaje = Double.parseDouble(scanner.nextLine());

            //entrar al proceso validando el porcentaje
            if(validarPorcentaje(porcentaje)) {
                //lista de nodos dentro de la etiqueta parent
                NodeList saleRecords = doc.getElementsByTagName("sale_record");
                for (int i = 0; i < saleRecords.getLength(); i++) {
                    Element saleRecord = (Element) saleRecords.item(i);

                    // departamento encontrado comparado con el del usuario
                    Element departmentElement = (Element) saleRecord.getElementsByTagName("department").item(0);
                    String departmentName = departmentElement.getTextContent();

                    //actualizar el registro dentro de la etiqueta <sales>
                    if (departmentName.equals(departamento)) {
                        Element salesElement = (Element) saleRecord.getElementsByTagName("sales").item(0);
                        double currentSales = Double.parseDouble(salesElement.getTextContent());
                        double newSales = ventaGenerada(porcentaje, currentSales);
                        salesElement.setTextContent(String.format("%8.2f", newSales)); //formateado igual que en el sales.xml original
                    }
                }

                //guardar registro en nuevo xml
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource src = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("src\\new_sales.xml"));
                transformer.transform(src, result);

                //output de validacion
                System.out.println("El registro de sales se actualizó con el %" + porcentaje + " en new_sales.xml.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



