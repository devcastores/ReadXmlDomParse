package com.grupocastores.mcc.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReadXmlDomParserFacturav2 {

	public static void main(String[] args) {

		/*
		 * Multi Root:
		 * https://stackoverflow.com/questions/30984236/xml-file-containing-multiple-root-elements/31274225
		 */
		
		/*
		 * FLECHA AMARILLA
		 */
		final String xmlStr_1 = "\r\n"
				+ "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:OTRO=\"http://www.pegasotecnologia.com/secfd/Schemas/FlechaAmarilla\" xmlns:adddomrec=\"http://www.pegasotecnologia.com/secfd/Schemas/AddendaDomicilioReceptor\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd http://www.pegasotecnologia.com/secfd/Schemas/FlechaAmarilla http://www.pegasotecnologia.com/secfd/Schemas/AddendaFlechaAmarillaXML.xsd http://www.pegasotecnologia.com/secfd/Schemas/AddendaDomicilioReceptor http://www.pegasotecnologia.com/secfd/schemas/AddendaDomicilioReceptor.xsd\" Version=\"3.3\" Serie=\"PFFABP\" Folio=\"3071497\" Fecha=\"2021-09-06T11:52:30\" Moneda=\"MXN\" TipoCambio=\"1\" SubTotal=\"538.79\" Total=\"625.00\" FormaPago=\"01\" TipoDeComprobante=\"I\" MetodoPago=\"PUE\" LugarExpedicion=\"37270\" NoCertificado=\"00001000000501619816\" Certificado=\"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\" Sello=\"LZxlW8f3jX/x5xxoq05A3R7wk7swGfxQJBcP5NZVlO69/uCAV8rcTLiQCtNEmv33dgF8mCp6cyPKW8xf/IqhiMSqxurvcc47SPGWUApEaqFMnb4SytGbJ6lL4V9hPYffFUqjBxwC7Ok7g5OV+l1eMqyDbROW1yQ+IYAfdpENgQc6LleyjAYB0eOrVObGkKxOKbxiSNWwMqt9VjFDk33saNet3F1XcDKS5udhqTI3LN1l996GTBBGwYuRmPcLLzra+vdcIBbKVd8P88OhY7N6hAbdU7dVQ09KLifGHOcv79oWmKrc+ycCidvtmTnETwDyvz0e8mdRMCJ8sXMYlMCqJQ==\">\r\n"
				+ "    <cfdi:Emisor Rfc=\"API6609273E0\" Nombre=\"AUTOBUSES DE LA PIEDAD S.A. DE C.V\" RegimenFiscal=\"624\" />\r\n"
				+ "    <cfdi:Receptor Rfc=\"TCB7401303A4\" Nombre=\"TRANSPORTES CASTORES DE BAJA CALIFORNIA, S.A. DE C.V.\" UsoCFDI=\"G03\" />\r\n"
				+ "    <cfdi:Conceptos>\r\n"
				+ "        <cfdi:Concepto ClaveProdServ=\"78111802\" NoIdentificacion=\"242931797919184\" Cantidad=\"1\" ClaveUnidad=\"E54\" Unidad=\"Viaje\" Descripcion=\"SERVICIO DE TRANSPORTE DE PRIMERA PLUS MEX-ON 04/09/2021 COMPLETO, JOSE I MALDONADO CAU. VTA REALIZADA EN Delg. Gustavo A. Madero Ciudad MEXICO, Av. 100 metros eje central L No. D, Col. Magdalena de las Salina C.P. 7760\" ValorUnitario=\"538.79\" Importe=\"538.79\">\r\n"
				+ "            <cfdi:Impuestos>\r\n"
				+ "                <cfdi:Traslados>\r\n"
				+ "                    <cfdi:Traslado Base=\"538.79\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"86.21\" />\r\n"
				+ "                </cfdi:Traslados>\r\n"
				+ "            </cfdi:Impuestos>\r\n"
				+ "        </cfdi:Concepto>\r\n"
				+ "    </cfdi:Conceptos>\r\n"
				+ "    <cfdi:Impuestos TotalImpuestosTrasladados=\"86.21\">\r\n"
				+ "        <cfdi:Traslados>\r\n"
				+ "            <cfdi:Traslado Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"86.21\" />\r\n"
				+ "        </cfdi:Traslados>\r\n"
				+ "    </cfdi:Impuestos>\r\n"
				+ "    <cfdi:Complemento>\r\n"
				+ "        <tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\" Version=\"1.1\" UUID=\"01692F44-CFD0-4EAC-9AAB-636EDAE183D7\" FechaTimbrado=\"2021-09-06T11:53:16\" RfcProvCertif=\"SST060807KU0\" SelloCFD=\"LZxlW8f3jX/x5xxoq05A3R7wk7swGfxQJBcP5NZVlO69/uCAV8rcTLiQCtNEmv33dgF8mCp6cyPKW8xf/IqhiMSqxurvcc47SPGWUApEaqFMnb4SytGbJ6lL4V9hPYffFUqjBxwC7Ok7g5OV+l1eMqyDbROW1yQ+IYAfdpENgQc6LleyjAYB0eOrVObGkKxOKbxiSNWwMqt9VjFDk33saNet3F1XcDKS5udhqTI3LN1l996GTBBGwYuRmPcLLzra+vdcIBbKVd8P88OhY7N6hAbdU7dVQ09KLifGHOcv79oWmKrc+ycCidvtmTnETwDyvz0e8mdRMCJ8sXMYlMCqJQ==\" NoCertificadoSAT=\"00001000000506202789\" SelloSAT=\"PreW3PkVHfbC9L9sWLluUhFX8xxHOaYDntz5mAzPVQFm5SR5Lt68EZqcP/Cs5Ez32s03MBizqj3y4glPCZ1uwCzjtU1F6l/K1cjxky4wkLrFiA6wYIDwg5T75EpQ3pQT3rNQqkSNgLTQFV3XTTBpnE4MWNJHaTdDk9MwQdW0dgJaUs39rvA68MuRzZDouHVpOQ7vdTtwOuEVseLoY0hOkGuYwJh5LlpdR5b74gxhpRnvXGm43DomeZKCzXlFnTznoohaN8S5OIRoNGnnDXeMQrGiExAdqzpfR81vsPyHa16diHDGoGAR8B4N22Jq7RQQvLtWo38bUpYWUb0mUHLUcw==\" />\r\n"
				+ "    </cfdi:Complemento>\r\n"
				+ "    <cfdi:Addenda>\r\n"
				+ "        <OTRO:AddendaEmisor>\r\n"
				+ "            <OTRO:RequestForPayment>\r\n"
				+ "                <OTRO:AddendaNotas correoReceptor=\"coordtrafico1_tol@castores.com.mx\" asuntoCorreo=\"Factura Eletrónica de Servicios Administrativos Grupo Flecha Amarilla S.A. de C.V.\" mensajeCorreo=\"Hola estimado cliente, el motivo de este mensaje es para comunicarle que se ha generado éxitosamente su factura electrónica y se adjunta el mismo en el presente mensaje. Le agradecemos su preferencia. Favor de no responder a este mensaje, para cualquier duda/aclaración, favor de hacerlo en la sección contáctanos del portal Primera Plus. Gracias.\" archivoAdjunto=\"AMBOS\" />\r\n"
				+ "            </OTRO:RequestForPayment>\r\n"
				+ "        </OTRO:AddendaEmisor>\r\n"
				+ "        <adddomrec:AddendaDomicilioReceptor calle=\"BLVD. JOSE MARIA MORELOS\" noExterior=\"2975\" colonia=\"ALFARO\" municipio=\"LEÓN\" estado=\"GUANAJUATO\" codigoPostal=\"37238\" />\r\n"
				+ "    </cfdi:Addenda>\r\n"
				+ "</cfdi:Comprobante>\r\n"
				+ "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:OTRO=\"http://www.pegasotecnologia.com/secfd/Schemas/FlechaAmarilla\" xmlns:adddomrec=\"http://www.pegasotecnologia.com/secfd/Schemas/AddendaDomicilioReceptor\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd http://www.pegasotecnologia.com/secfd/Schemas/FlechaAmarilla http://www.pegasotecnologia.com/secfd/Schemas/AddendaFlechaAmarillaXML.xsd http://www.pegasotecnologia.com/secfd/Schemas/AddendaDomicilioReceptor http://www.pegasotecnologia.com/secfd/schemas/AddendaDomicilioReceptor.xsd\" Version=\"3.3\" Serie=\"PFFABP\" Folio=\"3071497\" Fecha=\"2021-09-06T11:52:30\" Moneda=\"MXN\" TipoCambio=\"1\" SubTotal=\"538.79\" Total=\"625.00\" FormaPago=\"01\" TipoDeComprobante=\"I\" MetodoPago=\"PUE\" LugarExpedicion=\"37270\" NoCertificado=\"00001000000501619816\" Certificado=\"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\" Sello=\"LZxlW8f3jX/x5xxoq05A3R7wk7swGfxQJBcP5NZVlO69/uCAV8rcTLiQCtNEmv33dgF8mCp6cyPKW8xf/IqhiMSqxurvcc47SPGWUApEaqFMnb4SytGbJ6lL4V9hPYffFUqjBxwC7Ok7g5OV+l1eMqyDbROW1yQ+IYAfdpENgQc6LleyjAYB0eOrVObGkKxOKbxiSNWwMqt9VjFDk33saNet3F1XcDKS5udhqTI3LN1l996GTBBGwYuRmPcLLzra+vdcIBbKVd8P88OhY7N6hAbdU7dVQ09KLifGHOcv79oWmKrc+ycCidvtmTnETwDyvz0e8mdRMCJ8sXMYlMCqJQ==\">\r\n"
				+ "    <cfdi:Emisor Rfc=\"API6609273E0\" Nombre=\"AUTOBUSES DE LA PIEDAD S.A. DE C.V\" RegimenFiscal=\"624\" />\r\n"
				+ "    <cfdi:Receptor Rfc=\"TCB7401303A4\" Nombre=\"TRANSPORTES CASTORES DE BAJA CALIFORNIA, S.A. DE C.V.\" UsoCFDI=\"G03\" />\r\n"
				+ "    <cfdi:Conceptos>\r\n"
				+ "        <cfdi:Concepto ClaveProdServ=\"78111802\" NoIdentificacion=\"242931797919184\" Cantidad=\"1\" ClaveUnidad=\"E54\" Unidad=\"Viaje\" Descripcion=\"SERVICIO DE TRANSPORTE DE PRIMERA PLUS MEX-ON 04/09/2021 COMPLETO, JOSE I MALDONADO CAU. VTA REALIZADA EN Delg. Gustavo A. Madero Ciudad MEXICO, Av. 100 metros eje central L No. D, Col. Magdalena de las Salina C.P. 7760\" ValorUnitario=\"538.79\" Importe=\"538.79\">\r\n"
				+ "            <cfdi:Impuestos>\r\n"
				+ "                <cfdi:Traslados>\r\n"
				+ "                    <cfdi:Traslado Base=\"538.79\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"86.21\" />\r\n"
				+ "                </cfdi:Traslados>\r\n"
				+ "            </cfdi:Impuestos>\r\n"
				+ "        </cfdi:Concepto>\r\n"
				+ "    </cfdi:Conceptos>\r\n"
				+ "    <cfdi:Impuestos TotalImpuestosTrasladados=\"86.21\">\r\n"
				+ "        <cfdi:Traslados>\r\n"
				+ "            <cfdi:Traslado Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"86.21\" />\r\n"
				+ "        </cfdi:Traslados>\r\n"
				+ "    </cfdi:Impuestos>\r\n"
				+ "    <cfdi:Complemento>\r\n"
				+ "        <tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\" Version=\"1.1\" UUID=\"01692F44-CFD0-4EAC-9AAB-636EDAE183D7\" FechaTimbrado=\"2021-09-06T11:53:16\" RfcProvCertif=\"SST060807KU0\" SelloCFD=\"LZxlW8f3jX/x5xxoq05A3R7wk7swGfxQJBcP5NZVlO69/uCAV8rcTLiQCtNEmv33dgF8mCp6cyPKW8xf/IqhiMSqxurvcc47SPGWUApEaqFMnb4SytGbJ6lL4V9hPYffFUqjBxwC7Ok7g5OV+l1eMqyDbROW1yQ+IYAfdpENgQc6LleyjAYB0eOrVObGkKxOKbxiSNWwMqt9VjFDk33saNet3F1XcDKS5udhqTI3LN1l996GTBBGwYuRmPcLLzra+vdcIBbKVd8P88OhY7N6hAbdU7dVQ09KLifGHOcv79oWmKrc+ycCidvtmTnETwDyvz0e8mdRMCJ8sXMYlMCqJQ==\" NoCertificadoSAT=\"00001000000506202789\" SelloSAT=\"PreW3PkVHfbC9L9sWLluUhFX8xxHOaYDntz5mAzPVQFm5SR5Lt68EZqcP/Cs5Ez32s03MBizqj3y4glPCZ1uwCzjtU1F6l/K1cjxky4wkLrFiA6wYIDwg5T75EpQ3pQT3rNQqkSNgLTQFV3XTTBpnE4MWNJHaTdDk9MwQdW0dgJaUs39rvA68MuRzZDouHVpOQ7vdTtwOuEVseLoY0hOkGuYwJh5LlpdR5b74gxhpRnvXGm43DomeZKCzXlFnTznoohaN8S5OIRoNGnnDXeMQrGiExAdqzpfR81vsPyHa16diHDGoGAR8B4N22Jq7RQQvLtWo38bUpYWUb0mUHLUcw==\" />\r\n"
				+ "    </cfdi:Complemento>\r\n"
				+ "    <cfdi:Addenda>\r\n"
				+ "        <OTRO:AddendaEmisor>\r\n"
				+ "            <OTRO:RequestForPayment>\r\n"
				+ "                <OTRO:AddendaNotas correoReceptor=\"coordtrafico1_tol@castores.com.mx\" asuntoCorreo=\"Factura Eletrónica de Servicios Administrativos Grupo Flecha Amarilla S.A. de C.V.\" mensajeCorreo=\"Hola estimado cliente, el motivo de este mensaje es para comunicarle que se ha generado éxitosamente su factura electrónica y se adjunta el mismo en el presente mensaje. Le agradecemos su preferencia. Favor de no responder a este mensaje, para cualquier duda/aclaración, favor de hacerlo en la sección contáctanos del portal Primera Plus. Gracias.\" archivoAdjunto=\"AMBOS\" />\r\n"
				+ "            </OTRO:RequestForPayment>\r\n"
				+ "        </OTRO:AddendaEmisor>\r\n"
				+ "        <adddomrec:AddendaDomicilioReceptor calle=\"BLVD. JOSE MARIA MORELOS\" noExterior=\"2975\" colonia=\"ALFARO\" municipio=\"LEÓN\" estado=\"GUANAJUATO\" codigoPostal=\"37238\" />\r\n"
				+ "    </cfdi:Addenda>\r\n"
				+ "</cfdi:Comprobante>";
		
		/*
		 * HOTELES HACIENDA MAYA, S.A. DE C.V.
		 * TIPOCAMBIO: NULL
		 */
		final String xmlStr_2 = ""
				+ "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<cfdi:Comprobante xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd http://www.sat.gob.mx/implocal http://www.sat.gob.mx/sitio_internet/cfd/implocal/implocal.xsd\" Version=\"3.3\" Serie=\"B\" Folio=\"54738\" Fecha=\"2021-09-02T21:33:46\" FormaPago=\"01\" NoCertificado=\"00001000000504756617\" Certificado=\"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\" CondicionesDePago=\"114\" SubTotal=\"1657.86\" Moneda=\"MXN\" Total=\"2006.01\" TipoDeComprobante=\"I\" MetodoPago=\"PUE\" LugarExpedicion=\"97250\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:implocal=\"http://www.sat.gob.mx/implocal\" Sello=\"KQOtwDUa1VmOhaoeFeRsS+8OUyN9jjHe/Bk0qxxXRqnXl92wxu+y5waSTRlKjtEDv4VBgXZINXnTD6exKhM0Mp89DXA9XD/9U7SNyi480vh1hzrWKmd49va45JHWHnqJCkumhpofNZMVTiSpZKC8jWvrl1Gpp505GaEFjCGEWLs9XJuDYQnCcm9epefRjrN+e9NLt0g31/Az1ICF1R3LDyk2oduXHdJpsWfs5uPJClQ1/qwZRAtSVLhinb6F44mU740tuQHbOjmKOkvT7LYHPNvGvTw64MyxXin8OYjz93ULb3jK+djILSnKmowyp3cSu0FUuIrrcOt5O/wiI2zI6A==\">\r\n"
				+ "    <cfdi:Emisor Rfc=\"HHM110613V66\" Nombre=\"HOTELES HACIENDA MAYA, S.A. DE C.V.\" RegimenFiscal=\"601\"></cfdi:Emisor>\r\n"
				+ "    <cfdi:Receptor Rfc=\"TCB7401303A4\" Nombre=\"TRANSPORTES CASTORES DE BAJA CALIFORNIA S.A DE C.V.\" UsoCFDI=\"G03\"></cfdi:Receptor>\r\n"
				+ "    <cfdi:Conceptos>\r\n"
				+ "        <cfdi:Concepto ClaveProdServ=\"90111800\" Cantidad=\"1\" ClaveUnidad=\"E48\" Unidad=\"No aplica\" Descripcion=\"SERVICIO DE HOSPEDAJE\" ValorUnitario=\"1657.86\" Importe=\"1657.86\">\r\n"
				+ "            <cfdi:Impuestos>\r\n"
				+ "                <cfdi:Traslados>\r\n"
				+ "                    <cfdi:Traslado Base=\"1657.86\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"265.2576\"></cfdi:Traslado>\r\n"
				+ "                </cfdi:Traslados>\r\n"
				+ "            </cfdi:Impuestos>\r\n"
				+ "        </cfdi:Concepto>\r\n"
				+ "    </cfdi:Conceptos>\r\n"
				+ "    <cfdi:Impuestos TotalImpuestosTrasladados=\"265.26\">\r\n"
				+ "        <cfdi:Traslados>\r\n"
				+ "            <cfdi:Traslado Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"265.26\"></cfdi:Traslado>\r\n"
				+ "        </cfdi:Traslados>\r\n"
				+ "    </cfdi:Impuestos>\r\n"
				+ "    <cfdi:Complemento>\r\n"
				+ "        <tfd:TimbreFiscalDigital RfcProvCertif=\"TSP080724QW6\" Version=\"1.1\" UUID=\"73D3DF61-0F46-433D-8D9A-F3FF39B2EDF7\" FechaTimbrado=\"2021-09-02T21:33:50\" SelloCFD=\"KQOtwDUa1VmOhaoeFeRsS+8OUyN9jjHe/Bk0qxxXRqnXl92wxu+y5waSTRlKjtEDv4VBgXZINXnTD6exKhM0Mp89DXA9XD/9U7SNyi480vh1hzrWKmd49va45JHWHnqJCkumhpofNZMVTiSpZKC8jWvrl1Gpp505GaEFjCGEWLs9XJuDYQnCcm9epefRjrN+e9NLt0g31/Az1ICF1R3LDyk2oduXHdJpsWfs5uPJClQ1/qwZRAtSVLhinb6F44mU740tuQHbOjmKOkvT7LYHPNvGvTw64MyxXin8OYjz93ULb3jK+djILSnKmowyp3cSu0FUuIrrcOt5O/wiI2zI6A==\" NoCertificadoSAT=\"00001000000501960426\" SelloSAT=\"W8AIFDbJWWDtZr+U2Asy2aAPlzNBKe6ye/TbuLG4wNVbUbcspgftB+cgFe8lfj6yUqo793C48EduZOVyY/olT7Ww/B+dFDqoxEE+xSP5zyP4beSi40ljg7AHnZ6t23FuC0BhO/MA2FLB9FPHMvGiDbRmk5tv/31zNue6Gfxf0OBvzZojdfoJXdO49C/UMswu1fQHfnVjI/zRsxk2VygVaisSVpfCxmlqnZ2JyfKwlPiusI9jAIVS5f6Rxhu7bp0DQtkdiKtyX40ZFA6au/vUc3U+MbmxKmkzLLuvcesCmluTFOh32x91fGikSG/q1zRMTcY6aff7Q6RF6zNUf6F42Q==\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/timbrefiscaldigital/TimbreFiscalDigitalv11.xsd\" xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>\r\n"
				+ "        <implocal:ImpuestosLocales TotaldeTraslados=\"82.89\" TotaldeRetenciones=\"0.00\" version=\"1.0\">\r\n"
				+ "            <implocal:TrasladosLocales ImpLocTrasladado=\"ISH\" TasadeTraslado=\"5.00\" Importe=\"82.89\"></implocal:TrasladosLocales>\r\n"
				+ "        </implocal:ImpuestosLocales>\r\n"
				+ "    </cfdi:Complemento>\r\n"
				+ "</cfdi:Comprobante>\r\n"
				+ "";
		
		/*
		 * TRANSPORTE ETN
		 * SUCCESS
		 */
		final String xmlStr_3 = ""
				+ "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
				+ "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\" Version=\"3.3\" Fecha=\"2021-09-06T11:50:03\" Moneda=\"MXN\" TipoCambio=\"1\" SubTotal=\"435.34\" Total=\"505.00\" FormaPago=\"01\" TipoDeComprobante=\"I\" MetodoPago=\"PUE\" LugarExpedicion=\"07700\" NoCertificado=\"00001000000412606800\" Serie=\"B\" Folio=\"5560351\" Certificado=\"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\" Sello=\"JfNIUZixs2anDQ9eVZPLCOO1DApdqNGwZHmaFvBoVdbwN+J0HyCmJrpF5y6dZixb8g5l6YtLiRoLA73ORHALyt0iSk1FvdztAnPBcApTF4RntLtAaL8qmJXLKUJaNi9TOPhur87lGs+Vqwr4XvmrD0aegz7FEa7PL0XHKrrOlu+/rrKuK4f5n9czivCRRoENOeJpmVcSy8JTjuBE3qWMGUidl/bD3Vm4r6YSKzmsVYvjPydzAfrZ25Srqg/IYaaADaXsxm+foXJPjaQFGjjC2ElNctwXcnJgyKlOSjF4RRrQzJtLmcWJ9iyysSAMHjW0hmEZaR+2U237XJOe8eIMMg==\">\r\n"
				+ "    <cfdi:Emisor Rfc=\"TLU080610C81\" Nombre=\"ETN TURISTAR LUJO, S.A. DE C.V.\" RegimenFiscal=\"624\" />\r\n"
				+ "    <cfdi:Receptor Rfc=\"TCB7401303A4\" Nombre=\"TRANSPORTES CASTORES DE BAJA CALIFORNIA S.A DE C.V\" UsoCFDI=\"G03\" />\r\n"
				+ "    <cfdi:Conceptos>\r\n"
				+ "        <cfdi:Concepto ClaveProdServ=\"78111802\" Cantidad=\"1\" ClaveUnidad=\"E48\" Unidad=\"Unidad de servicio\" Descripcion=\"Servicios de buses con horarios programados\" ValorUnitario=\"435.34\" Importe=\"435.34\">\r\n"
				+ "            <cfdi:Impuestos>\r\n"
				+ "                <cfdi:Traslados>\r\n"
				+ "                    <cfdi:Traslado Base=\"435.34\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"69.66\" />\r\n"
				+ "                </cfdi:Traslados>\r\n"
				+ "            </cfdi:Impuestos>\r\n"
				+ "        </cfdi:Concepto>\r\n"
				+ "    </cfdi:Conceptos>\r\n"
				+ "    <cfdi:Impuestos TotalImpuestosTrasladados=\"69.66\">\r\n"
				+ "        <cfdi:Traslados>\r\n"
				+ "            <cfdi:Traslado Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"69.66\" />\r\n"
				+ "        </cfdi:Traslados>\r\n"
				+ "    </cfdi:Impuestos>\r\n"
				+ "    <cfdi:Complemento>\r\n"
				+ "        <tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\" Version=\"1.1\" UUID=\"A3CBB2D4-07F8-4426-BE4A-A20DAA609EB8\" FechaTimbrado=\"2021-09-06T11:50:03\" RfcProvCertif=\"SST060807KU0\" SelloCFD=\"JfNIUZixs2anDQ9eVZPLCOO1DApdqNGwZHmaFvBoVdbwN+J0HyCmJrpF5y6dZixb8g5l6YtLiRoLA73ORHALyt0iSk1FvdztAnPBcApTF4RntLtAaL8qmJXLKUJaNi9TOPhur87lGs+Vqwr4XvmrD0aegz7FEa7PL0XHKrrOlu+/rrKuK4f5n9czivCRRoENOeJpmVcSy8JTjuBE3qWMGUidl/bD3Vm4r6YSKzmsVYvjPydzAfrZ25Srqg/IYaaADaXsxm+foXJPjaQFGjjC2ElNctwXcnJgyKlOSjF4RRrQzJtLmcWJ9iyysSAMHjW0hmEZaR+2U237XJOe8eIMMg==\" NoCertificadoSAT=\"00001000000506202789\" SelloSAT=\"L1UF9wzmqCYxIJSI89qbllTl6jx702krUZQTAX3NBOGbAMWNZaOTtj4ARIoY3jarCC5Va700AV39juJnOwKMRTu1zUvdTDQLg/fqa2McS5IIIuf8UvxP3cDS+S9fldfrWS8agCjYp69jO8LAJrDW9mmNomQsbPZWGm5BdmDP2qQzs1Qh++HNVvq8Du5I2rlZpS0G0r7gTTdOahTnAuZ5i95DQy26lKCjlZ7p+lY4x3LQdWQm/bU8JXnYetuFapawEPW5lRZCbn0XWHSbyr3lJgC03wOjreqEGeYG7P2FHyzSCRj+yNE9M/2zSlPmqqjV7rO2wuUSLDD2x6770B+DNQ==\" />\r\n"
				+ "    </cfdi:Complemento>\r\n"
				+ "</cfdi:Comprobante>"
				+ "";
		
		/*
		 * taxi aeropuerto medira
		 * success
		 */
		final String xmlStr_4 = ""
				+ "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\" Version=\"3.3\" Serie=\"A\" Folio=\"1144\" Fecha=\"2021-09-06T11:46:11\" Sello=\"BgZHHo1D8Xxr0oy7AzMNvn4Ny+TxsJlyY8czObT38QShO6RN2HUGK/SNuqBkwSGU4DdWlJ93eSpv0AxWwMxXClNJMOOt5HTq+J2yj9wUUwnQOnM0iVZ7ZDkUunyu2m+/6m0P0xrzf/3w7uw6GMjQ14a7kXeKdeXlcuEfMvDidQJ1k0xAldgGr6KWOmfw1rM+uWldvKmONfkhog8kpwpWVua867sAuioNW35KO+R5iOjxC+imx5mpePJqGcYZIgHKesw08BSOROARwE+MPiCQ79MNpKhkQSnmVr3p81iUk6cGJObzQAlBkFUlLiZcl7DmOst+kQM/DLF1JxEvpqnSkw==\" FormaPago=\"01\" NoCertificado=\"00001000000503989962\" Certificado=\"MIIGSDCCBDCgAwIBAgIUMDAwMDEwMDAwMDA1MDM5ODk5NjIwDQYJKoZIhvcNAQELBQAwggGEMSAwHgYDVQQDDBdBVVRPUklEQUQgQ0VSVElGSUNBRE9SQTEuMCwGA1UECgwlU0VSVklDSU8gREUgQURNSU5JU1RSQUNJT04gVFJJQlVUQVJJQTEaMBgGA1UECwwRU0FULUlFUyBBdXRob3JpdHkxKjAoBgkqhkiG9w0BCQEWG2NvbnRhY3RvLnRlY25pY29Ac2F0LmdvYi5teDEmMCQGA1UECQwdQVYuIEhJREFMR08gNzcsIENPTC4gR1VFUlJFUk8xDjAMBgNVBBEMBTA2MzAwMQswCQYDVQQGEwJNWDEZMBcGA1UECAwQQ0lVREFEIERFIE1FWElDTzETMBEGA1UEBwwKQ1VBVUhURU1PQzEVMBMGA1UELRMMU0FUOTcwNzAxTk4zMVwwWgYJKoZIhvcNAQkCE01yZXNwb25zYWJsZTogQURNSU5JU1RSQUNJT04gQ0VOVFJBTCBERSBTRVJWSUNJT1MgVFJJQlVUQVJJT1MgQUwgQ09OVFJJQlVZRU5URTAeFw0yMDA1MTYxNzU2MTlaFw0yNDA1MTYxNzU2MTlaMIIBFTE9MDsGA1UEAxM0U09DSUVEQUQgQ09PUEVSQVRJVkEgREUgQVVUT1RSQU5TUE9SVEVTIEYuVS5ULlYuIFNDTDE9MDsGA1UEKRM0U09DSUVEQUQgQ09PUEVSQVRJVkEgREUgQVVUT1RSQU5TUE9SVEVTIEYuVS5ULlYuIFNDTDE9MDsGA1UEChM0U09DSUVEQUQgQ09PUEVSQVRJVkEgREUgQVVUT1RSQU5TUE9SVEVTIEYuVS5ULlYuIFNDTDElMCMGA1UELRMcU0NBNzgwMTI0SVgwIC8gQVVTSjU5MTIxOUo0MTEeMBwGA1UEBRMVIC8gQVVTSjU5MTIxOUhZTkdMTDA2MQ8wDQYDVQQLEwZVTklEQUQwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCOVOmGGXjSbxsbSOc+62MKsMettft/DmTjT8uhpZw7+W1VwUKzeFdj1tV4nn5zUboJAXSoRv5drkxhJ4Npt0YQCU8QtDI6rVa4jENt/+3NzGJPGNbEBeSqA9kVDXVuLu0Ya803EQj3yM8fnFO92XErlkU8iBjOyjgkrEuDPvq+503nW26rAGxgmwHIczzA0cbmXyEy4jPXqmz9XDkOXfIocg9s+fWcvZoX8D/BGNiO03dClsXPwt36ka6D6SA8r9dmXkTQQr5KXt4sKnq5QVHOInP83zN+CimQBT+ln75Dz6YKI2gyM6DSUKtFs0ry2EcWMyuyHmr9Og3GdAIL19K/AgMBAAGjHTAbMAwGA1UdEwEB/wQCMAAwCwYDVR0PBAQDAgbAMA0GCSqGSIb3DQEBCwUAA4ICAQAdF7R6kPPTlfrkmlMoesL1gQmtduDGaGYMj7/nqBUr0AAd83iwvAdE487uHtrZcW+Yk+85warZg4p/aii7PNBQVOdRkOWmBVvvvIncDc/P2uFfiW75+LEid7XrDkxezSKkhe1dDMLAiZjUfu+Xu0eOmu+0lRJIhiSs+CBXpoR5/C+e4K/bfQO47UQIzhIMMhIdN4I3lAgmqgou8HrKcAlUJwmeX6ZJmiGlkXfFSALb7hVZ/7Ju8K/MP/8+9foSlRlC5I2ssw022q2I1GiLRbgZgfRp//7ZK8nL1MC2siPw+BrNSgTwduvx/ROHKlBNiL6gZ5iF/Gw85Ot0LuWiMjm5rmzoqa7xVc+IWK8egmdcGOr4qzsPP0rsoBBdH+nDXwdQzR4SgDOfD4J4ObG0GAxaPb8HG/qk5nHEPUnbXNLnWZRf4AUyf1hXiEC6lYBHfCQb139bF4qQdB2U5ZSbdGTVz0CgkHUM4+aDwFNTVznAx2MzbddqhV6mz+aH6VPHyCA444rszxL5g444z8tMf3mUJPct1KB7gg01XUp+yeiBC1rBeYEJCCj12B8BBcy+9khxjkxP+yI/08QTxyhGRPzvfIKnhV0Y0taDDreeIKEmhYkDQnfH+buk6zESUaOoOle9lAizjXauuLRGUsOjI9ImbxzPK0sA2W5POFCRNiUOOg==\" SubTotal=\"155\" Moneda=\"MXN\" Total=\"155\" TipoDeComprobante=\"I\" MetodoPago=\"PUE\" LugarExpedicion=\"97295\">\r\n"
				+ "    <cfdi:Emisor Rfc=\"SCA780124IX0\" Nombre=\"SOCIEDAD COOPERATIVA DE AUTOTRANSPORTES F.U.T.V. SCL\" RegimenFiscal=\"620\" />\r\n"
				+ "    <cfdi:Receptor Rfc=\"TCB7401303A4\" Nombre=\"TRANSPORTES CASTORES DE BAJA CALIFORNIA S.A DE C.V\" UsoCFDI=\"G03\" />\r\n"
				+ "    <cfdi:Conceptos>\r\n"
				+ "        <cfdi:Concepto ClaveProdServ=\"78111804\" Cantidad=\"1\" ClaveUnidad=\"E48\" Descripcion=\"ZONA AEROPUERTO\" ValorUnitario=\"155\" Importe=\"155\" />\r\n"
				+ "    </cfdi:Conceptos>\r\n"
				+ "    <cfdi:Complemento>\r\n"
				+ "        <tfd:TimbreFiscalDigital xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\" Version=\"1.1\" UUID=\"94C2CE23-798F-41C2-8FB5-359765BFD399\" FechaTimbrado=\"2021-09-06T11:46:14\" SelloCFD=\"BgZHHo1D8Xxr0oy7AzMNvn4Ny+TxsJlyY8czObT38QShO6RN2HUGK/SNuqBkwSGU4DdWlJ93eSpv0AxWwMxXClNJMOOt5HTq+J2yj9wUUwnQOnM0iVZ7ZDkUunyu2m+/6m0P0xrzf/3w7uw6GMjQ14a7kXeKdeXlcuEfMvDidQJ1k0xAldgGr6KWOmfw1rM+uWldvKmONfkhog8kpwpWVua867sAuioNW35KO+R5iOjxC+imx5mpePJqGcYZIgHKesw08BSOROARwE+MPiCQ79MNpKhkQSnmVr3p81iUk6cGJObzQAlBkFUlLiZcl7DmOst+kQM/DLF1JxEvpqnSkw==\" NoCertificadoSAT=\"00001000000505600468\" SelloSAT=\"m6NzBDhZblqvsOekGFRLpmrb+IkpLfVupp5urN7TaxQ8ZAp70RRBWeL3GgcE/wyQ1lCaVPn2AS2KEjAANiD5del1jjhp+0k2gINL41wfjBjXx/WwKDMeg03KTN8KmniejaHJ2G9EO0O4GXWY/keYOqrfrhZfsqsN1wrcWv4Ys6v55I6mprsH3itmStahqX6rHtwFk3qE3LoaVwuH0EmO1xxbVUwWnp+vI1vlFf/HX3yYi6OXmn9Cy+N1st43H+Ij7q/CpmNUjERllgR31sIPChnd6NcndiQknoaO4pIVpKjo3C+Aq5QbZ2mh4VMwbIJleGNRLDI0EERytl0e7fhYEQ==\" RfcProvCertif=\"EME000602QR9\" />\r\n"
				+ "    </cfdi:Complemento>\r\n"
				+ "</cfdi:Comprobante>"
				+ "";
		/*
		 * Oxxo Gas
		 * SUCCESS
		 */
		final String xmlStr_5 = ""
				+ "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n"
				+ "<cfdi:Comprobante Serie=\"CLI\" Folio=\"12695068\" Fecha=\"2021-09-01T17:40:33\" SubTotal=\"86.50\" Moneda=\"MXN\" Total=\"100.00\" TipoDeComprobante=\"I\" FormaPago=\"01\" MetodoPago=\"PUE\" CondicionesDePago=\"Contado\" TipoCambio=\"1\" LugarExpedicion=\"37444\" Descuento=\"0.00\" xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:dvz=\"http://www.diverza.com/ns/addenda/diverza/1\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\" Version=\"3.3\" NoCertificado=\"00001000000413284971\" Certificado=\"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\" Sello=\"Id+0DoIbRge6/l+e+DYSsIgVquizRT7+2PHKHwtbbTthyFA4fRai6SLAnO6HBaN03q5cJZHl22NpqUr66x8parJWNcv0ovwvyxA+HQehZ1w/gRY9MtokLUtBdlLNx1ZYkTOJBW78v1dNmoXnfF0wV4xaiJgvl9Nt/mg+EpruxmJouy4XrNvSST03pj2AEPsQEe7Ocf+dHQ0OAtHZb5l+DF+tOyTVeFeEfYtjmh2wE9ZPRpFd4ezTuO1HLV/AEex+eXPSkVepPR4yM1vF1aFVuOL8zl3zPrjHlvPAgxuLS+xZjfpxzUFwPTFxFOcXyowBlSFp9jcY9AtPrOhVqYVenw==\">\r\n"
				+ "    <cfdi:Emisor Rfc=\"SGM950714DC2\" Nombre=\"Servicios Gasolineros de MÃ©xico SA de CV\" RegimenFiscal=\"623\"/>\r\n"
				+ "    <cfdi:Receptor Rfc=\"TCB7401303A4\" Nombre=\"TRASPORTES CASTORES DE BAJA CALIFORNIA SA DE CV\" UsoCFDI=\"G03\"/>\r\n"
				+ "    <cfdi:Conceptos>\r\n"
				+ "        <cfdi:Concepto ClaveProdServ=\"15101514\" ClaveUnidad=\"LTR\" NoIdentificacion=\"PL/7571/EXP/ES/2015-25575990\" Cantidad=\"4.7664\" Unidad=\"Litro\" Descripcion=\" MAGNA (LT)\" ValorUnitario=\"18.148469\" Importe=\"86.50\" Descuento=\"0.00\">\r\n"
				+ "            <cfdi:Impuestos>\r\n"
				+ "                <cfdi:Traslados>\r\n"
				+ "                    <cfdi:Traslado Base=\"84.3521\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"13.5\"/>\r\n"
				+ "                </cfdi:Traslados>\r\n"
				+ "            </cfdi:Impuestos>\r\n"
				+ "        </cfdi:Concepto>\r\n"
				+ "    </cfdi:Conceptos>\r\n"
				+ "    <cfdi:Impuestos TotalImpuestosTrasladados=\"13.50\">\r\n"
				+ "        <cfdi:Traslados>\r\n"
				+ "            <cfdi:Traslado Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"13.50\"/>\r\n"
				+ "        </cfdi:Traslados>\r\n"
				+ "    </cfdi:Impuestos>\r\n"
				+ "    <cfdi:Complemento>\r\n"
				+ "        <tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\" Version=\"1.1\" UUID=\"ae80fa70-2c1f-45c5-8e0c-37d31d04cf87\" RfcProvCertif=\"SNF171020F3A\" FechaTimbrado=\"2021-09-01T17:40:35\" SelloCFD=\"Id+0DoIbRge6/l+e+DYSsIgVquizRT7+2PHKHwtbbTthyFA4fRai6SLAnO6HBaN03q5cJZHl22NpqUr66x8parJWNcv0ovwvyxA+HQehZ1w/gRY9MtokLUtBdlLNx1ZYkTOJBW78v1dNmoXnfF0wV4xaiJgvl9Nt/mg+EpruxmJouy4XrNvSST03pj2AEPsQEe7Ocf+dHQ0OAtHZb5l+DF+tOyTVeFeEfYtjmh2wE9ZPRpFd4ezTuO1HLV/AEex+eXPSkVepPR4yM1vF1aFVuOL8zl3zPrjHlvPAgxuLS+xZjfpxzUFwPTFxFOcXyowBlSFp9jcY9AtPrOhVqYVenw==\" NoCertificadoSAT=\"00001000000414211380\" SelloSAT=\"oD67PuNNRLdZ46lLSgP1rESg+Im2KgFUoGyP3A1VxKiZfKbpo+hIoW13VtRKLi1lgG6SoZreDNlYHA6Wu1W4cFJV+c+Wq+ozF2Q/ruWXD5ngjTWI/1e+XgjPfoXZFxJ7zhGVfUg8jXBaYoMe58YqbqgorZ1l3R4L3Dx9kAsi7aBlrQmW+J9b1J8IIdjy0R4nmQygQbypEgR2AXpPqHS43Gr1io0nqgm8I+up91UgZGUl0viNGSeHsP1fQrn3y8mg2DX95OwsIwvZQLXjy6Vj+JNjDWPf8eXAHoL7q92k1OZnTcWTO5HpkwJP4mXUyLNn4/Qm8ljpAaXkrYKmEhn6Zg==\"/>\r\n"
				+ "    </cfdi:Complemento>\r\n"
				+ "    <cfdi:Addenda>\r\n"
				+ "        <dvz:diverza version=\"1.1\">\r\n"
				+ "            <dvz:generales totalConLetra=\"CIEN PESOS (00/100) M.N. \" observaciones=\"**Los importes son expresados en 2 decimales en el presente documento PDF**\"/>\r\n"
				+ "            <dvz:emisor>\r\n"
				+ "                <dvz:domicilioFiscalE calle=\"Edison\" numero=\"1235 Norte\" colonia=\"Talleres\" municipio=\"Monterrey\" estado=\"Nuevo Leon\" pais=\"Mexico\" codigoPostal=\"64000\"/>\r\n"
				+ "                <dvz:sucursalE alias=\"E11765\">\r\n"
				+ "                    <dvz:domicilioSucursal calle=\"\" numero=\"\" colonia=\"\" municipio=\"\" estado=\"\" pais=\"\" codigoPostal=\"37444\"/>\r\n"
				+ "                </dvz:sucursalE>\r\n"
				+ "            </dvz:emisor>\r\n"
				+ "            <dvz:receptor>\r\n"
				+ "                <dvz:datosContactoR/>\r\n"
				+ "                <dvz:domicilioFiscalR calle=\"BLVD JOSE MARIA MORELOS #2975\" colonia=\"COL. ALFARO\" municipio=\"LEON\" estado=\"GUANAJUATO\" pais=\"MÃ©xico\" codigoPostal=\"37238\"/>\r\n"
				+ "            </dvz:receptor>\r\n"
				+ "            <dvz:complemento>\r\n"
				+ "                <dvz:datosExtra atributo=\"No. Cliente\" valor=\"663829\"/>\r\n"
				+ "                <dvz:datosExtra atributo=\"TICKETS\" valor=\"25575990\"/>\r\n"
				+ "            </dvz:complemento>\r\n"
				+ "        </dvz:diverza>\r\n"
				+ "    </cfdi:Addenda>\r\n"
				+ "</cfdi:Comprobante>"
				+ "";

		/*
		 * others 
		 */
		final String xmlStr_6 =""
				+ "<?xml version=\"1.0\" encoding=\"UTF-8\"?><cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:implocal=\"http://www.sat.gob.mx/implocal\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" Certificado=\"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\" Fecha=\"2020-10-21T05:04:02\" Folio=\"70401\" FormaPago=\"01\" LugarExpedicion=\"58000\" MetodoPago=\"PUE\" Moneda=\"MXN\" NoCertificado=\"00001000000406794301\" Sello=\"vACxQFa9lyIoi1zC3lwV7RY0pKdiB0/UC5hO06pxWtElvmWiVOXCJwRQP/n30i4fmahNwrzLKLMcg9ZHbSKDbqLo0FwygyIcM5KVioiDbtt4p4UYGBai87g+tJPhEo7kPm/Zakrx4GzVLS8xJoLRYs9cdKQ+Ja2j7OONRSbXdFw/KxE//uG3JF9CwaF6Udlbigg6+RAM1FBrDD+I9ZBpTLVBsieXCVVNlkL4CRcoobRhHP7UHEvM12gunjhqalIMYkGwGIai+7a16CBUuuoooUHSQ6IbcBrEWiA3rI69NcsJdNkDuGNq80DcEspv3erKSsttNKC+8U9fuJmJFDH+vw==\" Serie=\"CMLM\" SubTotal=\"806.72\" TipoCambio=\"1\" TipoDeComprobante=\"I\" Total=\"960.00\" Version=\"3.3\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd  http://www.sat.gob.mx/implocal http://www.sat.gob.mx/sitio_internet/cfd/implocal/implocal.xsd\">\r\n"
				+ "  <cfdi:Emisor Nombre=\"GRUPO TARERIO SA DE CV\" RegimenFiscal=\"601\" Rfc=\"GTA960506KT2\"/>\r\n"
				+ "  <cfdi:Receptor Nombre=\"TRANSPORTES CASTORES DE BAJA CALIFORNIA, S.A. DE C.V.\" Rfc=\"TCB7401303A4\" UsoCFDI=\"G03\"/>\r\n"
				+ "  <cfdi:Conceptos>\r\n"
				+ "    <cfdi:Concepto Cantidad=\"1.00\" ClaveProdServ=\"90111500\" ClaveUnidad=\"E48\" Descripcion=\"RENTA HABITACION\" Importe=\"806.72\" NoIdentificacion=\"HAB\" Unidad=\"NA\" ValorUnitario=\"806.72\">\r\n"
				+ "      <cfdi:Impuestos>\r\n"
				+ "        <cfdi:Traslados>\r\n"
				+ "          <cfdi:Traslado Base=\"806.72\" Importe=\"129.08\" Impuesto=\"002\" TasaOCuota=\"0.160000\" TipoFactor=\"Tasa\"/>\r\n"
				+ "        </cfdi:Traslados>\r\n"
				+ "      </cfdi:Impuestos>\r\n"
				+ "    </cfdi:Concepto>\r\n"
				+ "  </cfdi:Conceptos>\r\n"
				+ "  <cfdi:Impuestos TotalImpuestosTrasladados=\"129.08\">\r\n"
				+ "    <cfdi:Traslados>\r\n"
				+ "      <cfdi:Traslado Importe=\"129.08\" Impuesto=\"002\" TasaOCuota=\"0.160000\" TipoFactor=\"Tasa\"/>\r\n"
				+ "    </cfdi:Traslados>\r\n"
				+ "  </cfdi:Impuestos>\r\n"
				+ "  <cfdi:Complemento>\r\n"
				+ "    <implocal:ImpuestosLocales TotaldeRetenciones=\"0.00\" TotaldeTraslados=\"24.20\" version=\"1.0\" xmlns:implocal=\"http://www.sat.gob.mx/implocal\">\r\n"
				+ "      <implocal:TrasladosLocales ImpLocTrasladado=\"ISH\" Importe=\"24.20\" TasadeTraslado=\"3.00\"/>\r\n"
				+ "    </implocal:ImpuestosLocales>\r\n"
				+ "  <tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" FechaTimbrado=\"2020-10-21T05:09:02\" NoCertificadoSAT=\"00001000000405148267\" RfcProvCertif=\"SEF100616AD2\" SelloCFD=\"vACxQFa9lyIoi1zC3lwV7RY0pKdiB0/UC5hO06pxWtElvmWiVOXCJwRQP/n30i4fmahNwrzLKLMcg9ZHbSKDbqLo0FwygyIcM5KVioiDbtt4p4UYGBai87g+tJPhEo7kPm/Zakrx4GzVLS8xJoLRYs9cdKQ+Ja2j7OONRSbXdFw/KxE//uG3JF9CwaF6Udlbigg6+RAM1FBrDD+I9ZBpTLVBsieXCVVNlkL4CRcoobRhHP7UHEvM12gunjhqalIMYkGwGIai+7a16CBUuuoooUHSQ6IbcBrEWiA3rI69NcsJdNkDuGNq80DcEspv3erKSsttNKC+8U9fuJmJFDH+vw==\" SelloSAT=\"NswzPZTSsGQyETfMLklQDpiZWozBYJy5h00Hfl+qPNwU/jenUhtVPBKPIFFvtrZwsIC4ZatcOA5eepRoMfNTW1NJGVs+sWlVWgRFm/yAEVALWmb8Rm8BHy2nlmdFNjOeDyNIFsNNAOS79hUQvTlYLDjFhfUyP8V6ROk22kBx53/UBYmMVq9ny9gSSLjCuNXn5sPcJTfBfDBiG13u0dCG+HTRkiqrUXk+7H9yPDcjGxI/SdBLQoeNL5KNFuYh4+e7EFrMUpg3Fj18+iJOPxJQKu8GPVD+y2LtnHRpKaTcRwxX/DDsQ65QZHCenCiQxbDaMuSbGLHqi0w6IHxgTTXzEQ==\" UUID=\"AE7AFF75-DA31-4ABE-96F6-ED186BDBA2B9\" Version=\"1.1\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\"/></cfdi:Complemento> <cfdi:Addenda> <apr version=\"1.0\"> <Addenda><rsrvCity>14606647</rsrvCity><rsrvGDS></rsrvGDS><cupon></cupon></Addenda></apr></cfdi:Addenda>\r\n"
				+ "</cfdi:Comprobante>";
		
		final String xmlStr_7 = ""
				+ "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\" Version=\"3.3\" NoCertificado=\"00001000000411530483\" Certificado=\"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\" Sello=\"LIx99bKzaj6wjMBDczOG6I1tSNgXalrMs9vql1jGZJnYsG0X76/kLqT3HUMyGsglILMRDDZrQVyBibonqOSlUgh4w4opFKRSPxD4sfyOiKzxRX6ZGoQLGQ+lfFpEup5mtHta+G2aW5fPAjXF+IGFTjDKqPJY/t+6evEr5r8tC/qqGfUIYOFuz5MD3XIz5kx3SqKcefQMTs+ahS5A/6cKLOM9kvzhZTOPSL2hJx9D925af1Ab0wVjh9V1sfcCPs5UdVTY7h8CC5ehCZjBR92CncK56HRJhWqoYGfHUip7JWTCPmolFDokwOSyjJ1pJqR0pfC7BlHce+go88s61OdQQg==\" LugarExpedicion=\"37520\" Serie=\"WW\" Folio=\"012381\" Fecha=\"2020-11-23T13:46:00\" TipoDeComprobante=\"I\" CondicionesDePago=\"CONTADO\" FormaPago=\"01\" MetodoPago=\"PUE\" SubTotal=\"433.14\" Moneda=\"MXN\" Total=\"500.05\">\r\n"
				+ "  <cfdi:Emisor Rfc=\"SAL940831BE7\" Nombre=\"SERVICIO AZTECA DE LEON, SA DE CV\" RegimenFiscal=\"601\" />\r\n"
				+ "  <cfdi:Receptor Rfc=\"TCB7401303A4\" Nombre=\"TRASPORTES CASTORESDE BAJA CALIFORNIA S.A DE CV\" UsoCFDI=\"G03\" />\r\n"
				+ "  <cfdi:Conceptos>\r\n"
				+ "    <cfdi:Concepto ClaveProdServ=\"15101514\" ClaveUnidad=\"LTR\" NoIdentificacion=\"PL/1643/EXP/ES/2015-02592927\" Cantidad=\"30.160000\" Unidad=\"LITRO\" Descripcion=\"GASOLINA PEMEX MAGNA\" ValorUnitario=\"14.361406\" Importe=\"433.14\">\r\n"
				+ "      <cfdi:Impuestos>\r\n"
				+ "        <cfdi:Traslados>\r\n"
				+ "          <cfdi:Traslado Base=\"418.21\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"66.91\" />\r\n"
				+ "        </cfdi:Traslados>\r\n"
				+ "      </cfdi:Impuestos>\r\n"
				+ "    </cfdi:Concepto>\r\n"
				+ "  </cfdi:Conceptos>\r\n"
				+ "  <cfdi:Impuestos TotalImpuestosTrasladados=\"66.91\">\r\n"
				+ "    <cfdi:Traslados>\r\n"
				+ "      <cfdi:Traslado Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"66.91\" />\r\n"
				+ "    </cfdi:Traslados>\r\n"
				+ "  </cfdi:Impuestos>\r\n"
				+ "  <cfdi:Complemento>\r\n"
				+ "    <tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\" Version=\"1.1\" SelloCFD=\"LIx99bKzaj6wjMBDczOG6I1tSNgXalrMs9vql1jGZJnYsG0X76/kLqT3HUMyGsglILMRDDZrQVyBibonqOSlUgh4w4opFKRSPxD4sfyOiKzxRX6ZGoQLGQ+lfFpEup5mtHta+G2aW5fPAjXF+IGFTjDKqPJY/t+6evEr5r8tC/qqGfUIYOFuz5MD3XIz5kx3SqKcefQMTs+ahS5A/6cKLOM9kvzhZTOPSL2hJx9D925af1Ab0wVjh9V1sfcCPs5UdVTY7h8CC5ehCZjBR92CncK56HRJhWqoYGfHUip7JWTCPmolFDokwOSyjJ1pJqR0pfC7BlHce+go88s61OdQQg==\" NoCertificadoSAT=\"00001000000504204441\" RfcProvCertif=\"CVD110412TF6\" UUID=\"D4018211-BA1D-4EB1-9914-0CFCE80EF2E7\" FechaTimbrado=\"2020-11-23T13:48:16\" SelloSAT=\"czbJcpAhtTgXklwtyNMl/pKHqoiRIqiWxgPYrl6BTb8LjPLq5qMyzOqknLASDK2RDUbW4cc41YOdU4zQzVkDZuAt1U6CFQMtNw1HniDjb7EMrtqHoBy2ePMC2vk4LzXDOC9UgPPmmWb/osp6MPhPt31hpvy76G38eypCtGg2ZHzr3mJGmhejKXpJ7AA6LYcTZEjbsN4zUqp7v9LosL7WhZZgy4Z0XhZByRFjwiC3Mo8ularF+1yRdC3ySE52Nm3rSWLUdPvuF5w+mVlX3RvG15Xw65TsThp6M20y7cf3I+OzCoSHc1ba6/h2mVrH6+EEEqdz6sTpLs8plzPhQ/of5Q==\" />\r\n"
				+ "  </cfdi:Complemento>\r\n"
				+ "  <cfdi:Addenda>\r\n"
				+ "    <Transaccion Folio=\"02592927\" FechaHora=\"2020-11-21T18:12:03\" Producto=\"GASOLINA PEMEX MAGNA\" CveSat=\"15101514\" Precio=\"16.58\" Litros=\"30.160\" Importe=\"500.05\" />\r\n"
				+ "  </cfdi:Addenda>\r\n"
				+ "</cfdi:Comprobante>";
		/*
		 * nota: no regresa iva
		 * si se da formato si regresa iva
		 */
		final String xmlStr_8 = ""
				+ "<?xml version=\"1.0\" encoding=\"utf-8\"?><cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\" Version=\"3.3\" Serie=\"BGDL\" Folio=\"7695\" Fecha=\"2021-03-01T12:45:00\" Sello=\"JTEpCMj7DR/uK6Q7UYx7e0GPb9kh2+Zp2Y8JUtIM3PG0dxiU9VpIgW77qg9Lk+LunbT6otFTrxgpfljaYGF+DTrfZvuwx4MOcdvgM8sCf/UXb5a1xfUadg4j/EQoydDGK6bywpG7M174dJHdiohbtQqp3/JhfGBgna1+RsNj7WBmWiOuMLIp/8HgSKE9skG+AupSYy8ENKUtZchWJfzy/4B+hUijMOgGcPdq079xTTpe2RDNm9CWbXVMVn2sp7fANNhBSkUco+EmVmAmA1coCXK0O6ZxdBAv5xm0Jr1Xk5NlyQDh/F13yv2RNOl936sP2xp6ig520SNa5++rdJlLaw==\" FormaPago=\"01\" NoCertificado=\"00001000000503561203\" Certificado=\"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\" SubTotal=\"849.14\" Moneda=\"MXN\" Total=\"985.00\" TipoDeComprobante=\"I\" MetodoPago=\"PUE\" LugarExpedicion=\"85000\"><cfdi:Emisor Rfc=\"ATU940621PU7\" Nombre=\"AUTOTRANSPORTES TUFESA SA DE CV\" RegimenFiscal=\"624\"/><cfdi:Receptor Rfc=\"TCB7401303A4\" Nombre=\"TRANSPORTES CASTORES DE BAJA CALIFORNIA S.A. DE C.V\" UsoCFDI=\"G03\"/><cfdi:Conceptos><cfdi:Concepto ClaveProdServ=\"78111802\" Cantidad=\"1.00\" ClaveUnidad=\"E48\" Unidad=\"SERVICIO\" Descripcion=\"SERVICIO DE VIAJE - BOLETO: 572977 - Guadalajara(Tlaquepaque) a Culiacan - MUÑOZMORALES JUAN\" ValorUnitario=\"849.14\" Importe=\"849.14\"><cfdi:Impuestos><cfdi:Traslados><cfdi:Traslado Base=\"849.14\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"135.86\"/></cfdi:Traslados></cfdi:Impuestos></cfdi:Concepto></cfdi:Conceptos><cfdi:Impuestos TotalImpuestosTrasladados=\"135.86\"><cfdi:Traslados><cfdi:Traslado Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"135.86\"/></cfdi:Traslados></cfdi:Impuestos><cfdi:Complemento><tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\" Version=\"1.1\" UUID=\"2E031852-1E89-40A5-AAEE-E54EE28CEC8C\" FechaTimbrado=\"2021-03-01T13:45:49\" RfcProvCertif=\"PPD101129EA3\" SelloCFD=\"JTEpCMj7DR/uK6Q7UYx7e0GPb9kh2+Zp2Y8JUtIM3PG0dxiU9VpIgW77qg9Lk+LunbT6otFTrxgpfljaYGF+DTrfZvuwx4MOcdvgM8sCf/UXb5a1xfUadg4j/EQoydDGK6bywpG7M174dJHdiohbtQqp3/JhfGBgna1+RsNj7WBmWiOuMLIp/8HgSKE9skG+AupSYy8ENKUtZchWJfzy/4B+hUijMOgGcPdq079xTTpe2RDNm9CWbXVMVn2sp7fANNhBSkUco+EmVmAmA1coCXK0O6ZxdBAv5xm0Jr1Xk5NlyQDh/F13yv2RNOl936sP2xp6ig520SNa5++rdJlLaw==\" NoCertificadoSAT=\"00001000000504204971\" SelloSAT=\"V30UdxVFoTYYZms/mvW53CIWkDotoO7odqysGu+Ou481Mv46BiocDj2cz2AcPC1UFI3qFYydUslzuAYEXeJzbVWK1hJ48XF2PiKPIPq1TN3cdZidSC+xl3awe2yX2pE6ImfQUcAq4UqL4qxQxcKpYqH9f5k67391f2yzYFtGBIlNR4u2VYz/yUtjH5usfkuLewqp2Fsvc8BuysKJrhYt22GFvuc8sbPzL0y3AVCxDo1Y6GPLQIvOxPQgoF3AGb7a9EsqUgDIS01dra1GoO8tXBHBNP2DbWMJepVz3ke/kB0x/l3TWjJHb0XiYH9z4LnHKmvGvL/mkyNYfARHlohS4Q==\"/></cfdi:Complemento></cfdi:Comprobante>\r\n"
				+ "";
		/*
		 * nota: no regresa iva
		 */
		final String xmlStr_9 = ""
				+ "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:OTRO=\"http://www.pegasotecnologia.com/secfd/Schemas/FlechaAmarilla\" xmlns:adddomrec=\"http://www.pegasotecnologia.com/secfd/Schemas/AddendaDomicilioReceptor\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd http://www.pegasotecnologia.com/secfd/Schemas/FlechaAmarilla http://www.pegasotecnologia.com/secfd/Schemas/AddendaFlechaAmarillaXML.xsd http://www.pegasotecnologia.com/secfd/Schemas/AddendaDomicilioReceptor http://www.pegasotecnologia.com/secfd/schemas/AddendaDomicilioReceptor.xsd\" Version=\"3.3\" Serie=\"PFFABP\" Folio=\"2758694\" Fecha=\"2021-03-01T12:41:46\" Moneda=\"MXN\" TipoCambio=\"1\" SubTotal=\"378.45\" Total=\"439.00\" FormaPago=\"01\" TipoDeComprobante=\"I\" MetodoPago=\"PUE\" LugarExpedicion=\"37270\" NoCertificado=\"00001000000501619816\" Certificado=\"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\" Sello=\"HfOIBuNB2VL0OcPUGxY2pVOPCvJ9nBNU0zkukdgu7MgKS1wBpdR0i1VcOlDSlcNYwrnA+8luylcrPaiu/Y/rYPZypnMiF78rCEVwMtvenBeef6DwHgVXXEG2XE/VTEvtSWpfEmKCuAQZ/CCrlDSqs4DDygcNxnJTg/kyNHW7prbOOyzzEO2OG9Wgar4+isi8vD0D0rLwU7kD1xd26s71TvjnngWxlBCL6WkCFYC2BBsZVlyrFMoPDXh/RszQwItU12ZSzGKneOm0YvpnsFIfg7Th+/RmdfecAiSxJxEBVXvoo0R5FvGJKg5MpOWl2TC61Xj/Yj5F1/5SWUur2Zn3Qg==\"><cfdi:Emisor Rfc=\"API6609273E0\" Nombre=\"AUTOBUSES DE LA PIEDAD S.A. DE C.V\" RegimenFiscal=\"624\" /><cfdi:Receptor Rfc=\"TCB7401303A4\" Nombre=\"TRANSPORTES CASTORES DE BAJA CALIFORNIA SA DE CV\" UsoCFDI=\"G03\" /><cfdi:Conceptos><cfdi:Concepto ClaveProdServ=\"78111802\" NoIdentificacion=\"279780523739225\" Cantidad=\"1\" ClaveUnidad=\"E54\" Unidad=\"Viaje\" Descripcion=\"SERVICIO DE TRANSPORTE DE PRIMERA PLUS GUAD-ON 26/02/2021 COMPLETO, CARLOS MUÑOZ. VTA REALIZADA EN GUADALAJARA JALISCO, Carret. Libre Zapotlanejo-Gu No. , Carranza C.P. 45500\" ValorUnitario=\"378.45\" Importe=\"378.45\"><cfdi:Impuestos><cfdi:Traslados><cfdi:Traslado Base=\"378.45\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"60.55\" /></cfdi:Traslados></cfdi:Impuestos></cfdi:Concepto></cfdi:Conceptos><cfdi:Impuestos TotalImpuestosTrasladados=\"60.55\"><cfdi:Traslados><cfdi:Traslado Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"60.55\" /></cfdi:Traslados></cfdi:Impuestos><cfdi:Complemento><tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\" Version=\"1.1\" UUID=\"F4A43EED-3EB3-42B6-B1FE-63FD7BD6699E\" FechaTimbrado=\"2021-03-01T12:42:42\" RfcProvCertif=\"SST060807KU0\" SelloCFD=\"HfOIBuNB2VL0OcPUGxY2pVOPCvJ9nBNU0zkukdgu7MgKS1wBpdR0i1VcOlDSlcNYwrnA+8luylcrPaiu/Y/rYPZypnMiF78rCEVwMtvenBeef6DwHgVXXEG2XE/VTEvtSWpfEmKCuAQZ/CCrlDSqs4DDygcNxnJTg/kyNHW7prbOOyzzEO2OG9Wgar4+isi8vD0D0rLwU7kD1xd26s71TvjnngWxlBCL6WkCFYC2BBsZVlyrFMoPDXh/RszQwItU12ZSzGKneOm0YvpnsFIfg7Th+/RmdfecAiSxJxEBVXvoo0R5FvGJKg5MpOWl2TC61Xj/Yj5F1/5SWUur2Zn3Qg==\" NoCertificadoSAT=\"00001000000506202789\" SelloSAT=\"R5An7kq64wyUpnk1kRlrPKaWi96AaopccbgZQe4sjOz5JGUzjuVw+RIJP9DPP3bgOMtVvwxwbdoxuOhQwosXARoykXUdFsRjMRwXbqXW/wPMrV3KVuA/gJBr2VafB1D/zvyuAfeXw3Q0q4Tco4fQdbmoYy/6womZN7QXLEvnYwKIF35TqzAvzpCWBc23Y0msJg5ZUk/phlhVVJ4PJjoK5G4szxCChLmcrX2Iq+rGD+tjC+CKMtheYSizwzvLe7t7N0N+lNBJKaYEbho8De1jWV3a80PEVq1CjbMNDjwzIh2icggXvPbUNb/zSsLdYccDD8vsbf5f80uQzaiB4RamWg==\" /></cfdi:Complemento><cfdi:Addenda><OTRO:AddendaEmisor><OTRO:RequestForPayment><OTRO:AddendaNotas correoReceptor=\"telecomunicacionesti_lem@castores.com.mx\" asuntoCorreo=\"Factura Eletrónica de Servicios Administrativos Grupo Flecha Amarilla S.A. de C.V.\" mensajeCorreo=\"Hola estimado cliente, el motivo de este mensaje es para comunicarle que se ha generado éxitosamente su factura electrónica y se adjunta el mismo en el presente mensaje. Le agradecemos su preferencia. Favor de no responder a este mensaje, para cualquier duda/aclaración, favor de hacerlo en la sección contáctanos del portal Primera Plus. Gracias.\" archivoAdjunto=\"AMBOS\" /></OTRO:RequestForPayment></OTRO:AddendaEmisor><adddomrec:AddendaDomicilioReceptor calle=\"BLVD JOSE MARIA MORELOS\" noExterior=\"2975\" colonia=\"ALFARO\" municipio=\"LEÓN\" estado=\"GUANAJUATO\" codigoPostal=\"37238\" /></cfdi:Addenda></cfdi:Comprobante>\r\n"
				+ "";
		
		final String xmlStr_10 = ""
				+ "<?xml version=\"1.0\" encoding=\"utf-8\"?><cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\" Version=\"3.3\" Fecha=\"2021-03-01T11:07:36\" Moneda=\"MXN\" TipoCambio=\"1\" SubTotal=\"380.17\" Total=\"441.00\" FormaPago=\"28\" TipoDeComprobante=\"I\" MetodoPago=\"PUE\" LugarExpedicion=\"07700\" NoCertificado=\"00001000000412606800\" Serie=\"B\" Folio=\"5355004\" Certificado=\"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\" Sello=\"BIY45qxEqvAfuHdzKpoiGW/dLmf7g7uSca34L6M/BswBaOSmtLUw3x4L/3Bc53f4/V4MsGUZ8l/xiss3k0gqdNLXlfXorP7/6RsQ0mQMMjjPq7UH6L8+9VnqiEZGUnEdopevZVW8xwcETNZrVPbkluItr1yLS9opbkJeelaLWiT2GcjGFclg9uTqRIBlRxbOmzOxqkpXzbWylRRES/HbM6UCDrqyIumGgaaCsylEkcG5RTo9V42hOZxqM5tECdWNmkGzvtW7fLvHd/AIq2dcrCBJElegZdN+jouBlAJ3c0QQHeRIH3LBtUlcA1JMsUvklD5CxxkXv7KnQuHZJsJdfQ==\">\r\n"
				+ "  <cfdi:Emisor Rfc=\"TLU080610C81\" Nombre=\"ETN TURISTAR LUJO, S.A. DE C.V.\" RegimenFiscal=\"624\" />\r\n"
				+ "  <cfdi:Receptor Rfc=\"TCB7401303A4\" Nombre=\"TRANSPORTES CASTORES DE BAJA CALIFORNIA S.A.DE.C.V\" UsoCFDI=\"G03\" />\r\n"
				+ "  <cfdi:Conceptos>\r\n"
				+ "    <cfdi:Concepto ClaveProdServ=\"78111802\" Cantidad=\"1\" ClaveUnidad=\"E48\" Unidad=\"Unidad de servicio\" Descripcion=\"Servicios de buses con horarios programados\" ValorUnitario=\"380.17\" Importe=\"380.17\">\r\n"
				+ "      <cfdi:Impuestos>\r\n"
				+ "        <cfdi:Traslados>\r\n"
				+ "          <cfdi:Traslado Base=\"380.17\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"60.83\" />\r\n"
				+ "        </cfdi:Traslados>\r\n"
				+ "      </cfdi:Impuestos>\r\n"
				+ "    </cfdi:Concepto>\r\n"
				+ "  </cfdi:Conceptos>\r\n"
				+ "  <cfdi:Impuestos TotalImpuestosTrasladados=\"60.83\">\r\n"
				+ "    <cfdi:Traslados>\r\n"
				+ "      <cfdi:Traslado Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"60.83\" />\r\n"
				+ "    </cfdi:Traslados>\r\n"
				+ "  </cfdi:Impuestos>\r\n"
				+ "  <cfdi:Complemento>\r\n"
				+ "    <tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\" Version=\"1.1\" UUID=\"31A40632-50B7-440E-B4A6-0F61D2A9E3D9\" FechaTimbrado=\"2021-03-01T11:07:36\" RfcProvCertif=\"SST060807KU0\" SelloCFD=\"BIY45qxEqvAfuHdzKpoiGW/dLmf7g7uSca34L6M/BswBaOSmtLUw3x4L/3Bc53f4/V4MsGUZ8l/xiss3k0gqdNLXlfXorP7/6RsQ0mQMMjjPq7UH6L8+9VnqiEZGUnEdopevZVW8xwcETNZrVPbkluItr1yLS9opbkJeelaLWiT2GcjGFclg9uTqRIBlRxbOmzOxqkpXzbWylRRES/HbM6UCDrqyIumGgaaCsylEkcG5RTo9V42hOZxqM5tECdWNmkGzvtW7fLvHd/AIq2dcrCBJElegZdN+jouBlAJ3c0QQHeRIH3LBtUlcA1JMsUvklD5CxxkXv7KnQuHZJsJdfQ==\" NoCertificadoSAT=\"00001000000506202789\" SelloSAT=\"A1Jfn2r/e6VVePCjUuRGHdlU2qZpLfMSZ4ljUmI55h5k9mJXrK7cygvCTMHBJbXeXp6ITGxOS4v+DZ9l9pLqv/nKvL/Tdwdf8+wePANgiEZwtMgrSEkTvi/OmF1U8kf1M4i/2OZaNU8NtVBPAf9S4A8dtnlYtdNZ+Sq7mDN5aDr4mKB79CdYe65YMhQpi4DGeE6eM2cyw0dUwzsMzEuKPRSmaITMcTitkEvGPtd6UgshZ1tY38siUzrC63YH16MYtCphRq99k0gTNL7Cucb+jivF6dZPG876tp9t3KSUFtjJV/EXgWxMaqZEdxyDvlJgP2IHcX7hwTZdUmaHSO5l/w==\" />\r\n"
				+ "  </cfdi:Complemento>\r\n"
				+ "</cfdi:Comprobante>";
		
		final String xmlStr_11 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd http://www.sat.gob.mx/implocal http://www.sat.gob.mx/sitio_internet/cfd/implocal/implocal.xsd\" xmlns:implocal=\"http://www.sat.gob.mx/implocal\" Version=\"3.3\" Serie=\"HPV\" Folio=\"9400\" Fecha=\"2021-05-21T09:01:46\" Sello=\"Xw/VYUCDjnI+V+icrGOHqmx3/AOe2xORkyH1ZOksp/bUUreXXSNsBIW/mre7tsx52/xSSKzUScpawfzvxqf4BBzHzR//P6C/VoZYGHkM0iEk8/cJ0OoKgKmGKot1rA4B0A5kNluVEovWfZlBVj2ZZSNCp6zwYsKdTuS8dlWhapQvsQu0XUaQMWDH50U72IUXYLtoU57rh6i9eut5QU0GU5o3Zdvuj/j+9CMi8tcLLbmH/Dr0ZEoachR+U9WuuHU1+jwTm4h1DWcK0S8sEnrPpHr4Id7QWewCaz/073VKSM88qnEhmipWM9sLVimGJRIonbJTpqE8wmqesh8VlqOuJQ==\" FormaPago=\"01\" NoCertificado=\"00001000000409670692\" Certificado=\"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\" SubTotal=\"3209.94\" Moneda=\"MXN\" Total=\"3815.00\" TipoDeComprobante=\"I\" MetodoPago=\"PUE\" LugarExpedicion=\"45560\">  <cfdi:Emisor Rfc=\"HPV1710237V4\" Nombre=\"HOTEL POSADA VIRREYES S.A. DE C.V.\" RegimenFiscal=\"601\"/>  <cfdi:Receptor Rfc=\"TCB7401303A4\" Nombre=\"TRANSPORTES CASTORES DE BAJA CALIFORNIA, SA DE CV\" UsoCFDI=\"G03\"/>  <cfdi:Conceptos>    <cfdi:Concepto ClaveProdServ=\"90111800\" NoIdentificacion=\"17/05/21,RH,118\" Cantidad=\"1.00\" ClaveUnidad=\"E48\" Unidad=\"Servicios\" Descripcion=\"RENTA HABITACION\" ValorUnitario=\"764.71\" Importe=\"764.71\">      <cfdi:Impuestos>        <cfdi:Traslados>          <cfdi:Traslado Base=\"764.71\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"122.35\"/>        </cfdi:Traslados>      </cfdi:Impuestos>    </cfdi:Concepto>    <cfdi:Concepto ClaveProdServ=\"90111800\" NoIdentificacion=\"18/05/21,RH,118\" Cantidad=\"1.00\" ClaveUnidad=\"E48\" Unidad=\"Servicios\" Descripcion=\"RENTA HABITACION\" ValorUnitario=\"764.71\" Importe=\"764.71\">      <cfdi:Impuestos>        <cfdi:Traslados>          <cfdi:Traslado Base=\"764.71\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"122.35\"/>        </cfdi:Traslados>      </cfdi:Impuestos>    </cfdi:Concepto>    <cfdi:Concepto ClaveProdServ=\"90111800\" NoIdentificacion=\"19/05/21,RH,118\" Cantidad=\"1.00\" ClaveUnidad=\"E48\" Unidad=\"Servicios\" Descripcion=\"RENTA HABITACION\" ValorUnitario=\"764.71\" Importe=\"764.71\">      <cfdi:Impuestos>        <cfdi:Traslados>          <cfdi:Traslado Base=\"764.71\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"122.35\"/>        </cfdi:Traslados>      </cfdi:Impuestos>    </cfdi:Concepto>    <cfdi:Concepto ClaveProdServ=\"90111800\" NoIdentificacion=\"20/05/21,RP,118\" Cantidad=\"1.00\" ClaveUnidad=\"E48\" Unidad=\"Servicios\" Descripcion=\"PAQUETES\" ValorUnitario=\"915.81\" Importe=\"915.81\">      <cfdi:Impuestos>        <cfdi:Traslados>          <cfdi:Traslado Base=\"915.81\" Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"146.53\"/>        </cfdi:Traslados>      </cfdi:Impuestos>    </cfdi:Concepto>  </cfdi:Conceptos>  <cfdi:Impuestos TotalImpuestosTrasladados=\"513.58\">    <cfdi:Traslados>      <cfdi:Traslado Impuesto=\"002\" TipoFactor=\"Tasa\" TasaOCuota=\"0.160000\" Importe=\"513.58\"/>    </cfdi:Traslados>  </cfdi:Impuestos>  <cfdi:Complemento>    <implocal:ImpuestosLocales version=\"1.0\" TotaldeTraslados=\"91.48\" TotaldeRetenciones=\"0.00\">      <implocal:TrasladosLocales ImpLocTrasladado=\"I.S.H.\" TasadeTraslado=\"3.00\" Importe=\"91.48\"/>    </implocal:ImpuestosLocales>  <tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\" Version=\"1.1\" FechaTimbrado=\"2021-05-21T09:01:55\" UUID=\"a1ebea89-fc43-47c6-bf4f-553541b6e285\" NoCertificadoSAT=\"00001000000507247013\" SelloCFD=\"Xw/VYUCDjnI+V+icrGOHqmx3/AOe2xORkyH1ZOksp/bUUreXXSNsBIW/mre7tsx52/xSSKzUScpawfzvxqf4BBzHzR//P6C/VoZYGHkM0iEk8/cJ0OoKgKmGKot1rA4B0A5kNluVEovWfZlBVj2ZZSNCp6zwYsKdTuS8dlWhapQvsQu0XUaQMWDH50U72IUXYLtoU57rh6i9eut5QU0GU5o3Zdvuj/j+9CMi8tcLLbmH/Dr0ZEoachR+U9WuuHU1+jwTm4h1DWcK0S8sEnrPpHr4Id7QWewCaz/073VKSM88qnEhmipWM9sLVimGJRIonbJTpqE8wmqesh8VlqOuJQ==\" SelloSAT=\"sQ1czU4Pnq0FKI0T9/4SfKjg/zkRJiVf6GixVHTQfup8NKZD16g2fvvJb2zw2tBOcXuA8kfP9dKZfbfQ8SXGXxnbUwbAdw1TQf0rzmaVpb5J0GQMxbWjkd4wpwFMrWl3btanxJTlHL+vq5Ae8ay6nAblR9DFukrzNgODIby6C/cyK35UbHsFAqtpWPhnP7P/kR+gBu5CYcDooCzmPGh2fun/Urou09+cuCxKNtIA17y8SXEVW7NkhGznSZHJVPptS/DBDOprRO1XO/bqC1lmh6b+mY2y97gm1lVT2sN2+/HqkWuppnjrDejvdmpIeRtndtllM6IpZAr2ndhsQLe9DA==\" RfcProvCertif=\"CAD100607RY8\" /></cfdi:Complemento></cfdi:Comprobante>\r\n"
				+ "";
		
		try {

			/*
			 * >>> TEST
			 * select string xmlStr
			 */
			
			String xmlStr = xmlStr_8;
			
			/*
			 * Remove XML version
			 */
			xmlStr = xmlStr.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
			
			/*
			 * MultiRootXML Read Support
			 */
	        Document doc = convertStringToDocumentv2(xmlStr);
	        
	        /*
	         * OneRoot Read Support
	         */
	        //Document doc = convertStringToDocument(xmlStr_2);
	        
		    // optional, but recommended
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
	        
	        NodeList rootNodes = doc.getDocumentElement().getChildNodes();
			
	        if (rootNodes != null && rootNodes.getLength() > 0) {
		        for(int x=0; x<rootNodes.getLength(); x++){
		        	
		            Node rNode = rootNodes.item(x);
		            if(rNode.getNodeType()==Node.ELEMENT_NODE){
		            	
		            	/*
		            	 * cfdi:Comprobante
		            	 */
		            	NodeList listtest = doc.getElementsByTagName("cfdi:Comprobante");
		            	Node nNode = listtest.item(0);
		            	
		                System.out.println("Root: "+x+"::::::::::V2");
		                
		                System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
		    			System.out.println("------");
		    			
		    			String moneda = ((Element) nNode).getAttribute("Moneda");
		    			if(moneda.equals("")) {moneda = "N/A";}
		    			
		    			String tipoCambio = ((Element) nNode).getAttribute("TipoCambio");
		    			if(tipoCambio.equals("")) {tipoCambio = "0";}
		    			
		    			String subTotal = ((Element) nNode).getAttribute("SubTotal");
		    			if(subTotal.equals("")) {subTotal = "0";}
		    			
		    			String total = ((Element) nNode).getAttribute("Total");
		    			if(total.equals("")) {total = "0";}
		    			
		    			System.out.println("Moneda: " + moneda);
		    			System.out.println("TipoCambio: " + tipoCambio);
		    			System.out.println("Subtotal: " + subTotal);
		    			System.out.println("Total: " + total);
		    			
		    			/*
		    			 * cfdi:Impuestos
		    			 */
		    			NodeList list = doc.getElementsByTagName("cfdi:Impuestos");
		    			for (int i = 0; i < 1; i++) {
		    				if (list.item(i) != null && !list.item(i).getTextContent().equals("")) {
		    						
		    					NodeList child = list.item(i).getChildNodes();
		    					for (int j = 0; j < child.getLength(); j++) {
		    						/*
		    						 * cfdi:Traslados
		    						 */
		    						if (child.item(j).getNodeName().equals("cfdi:Traslados")) {
		    					
		    							NodeList child2 = child.item(j).getChildNodes();
		    							for (int k = 0; k < child2.getLength(); k++) {
		    								/*
		    								 * cfdi:Traslado
		    								 */
		    								if (child2.item(k).getNodeName().equals("cfdi:Traslado")) {
		    									
		    									String impuesto = ((Element) (child2.item(k))).getAttribute("Impuesto");
		    									System.out.println("Impuesto : " + impuesto);
		    									String importe = ((Element) (child2.item(k))).getAttribute("Importe");
		    									System.out.println("IVA : " + importe);
		    									String tasaOCuota = ((Element) (child2.item(k))).getAttribute("TasaOCuota");
		    									System.out.println("TasaOCuota : " + tasaOCuota);
		    									
		    								}
		    							}
		    						}
		    					}
		    				}
		    			}
		    			 break;
		            }
		           
		        }
	        }

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Method para convertir String a Documento
	 * @param xmlStr
	 * @return
	 */
	private static Document convertStringToDocument(String xmlStr) {
		// Instantiate the Factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			// optional, but recommended
			// process XML securely, avoid attacks like XML External Entities (XXE)
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			builder = factory.newDocumentBuilder();
			// parse XML file
			Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Method para convertir String a Documento v2
	 * @param xmlStr
	 * @return
	 */
	private static Document convertStringToDocumentv2(String xmlStr) {
		
		/*
		 * Read MultiRoot
		 * Se agrega un nodo Root para poder leer el XML
		 */
		List<InputStream> streams = Arrays.asList(
	            new ByteArrayInputStream("<cfdi:Root>".getBytes()),
	            new ByteArrayInputStream(xmlStr.getBytes()),
	            new ByteArrayInputStream("</cfdi:Root>".getBytes())
	    );
		
		InputStream new_xmlStr = new SequenceInputStream(Collections.enumeration(streams));
		
		// Instantiate the Factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		
		try {
			
			// optional, but recommended
			// process XML securely, avoid attacks like XML External Entities (XXE)
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			
			builder = factory.newDocumentBuilder();
			
			// parse XML file
			Document doc = builder.parse(new InputSource(new_xmlStr));
			
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Method para convertir Documento a String
	 * <<< Da formato al string plano
	 * NOTA: PUEDE SOLUCIONAR EL PROBLEMA DE RESIBIR UN TEXTO COMO EL NUMERO 8 Y 9
	 * @param doc
	 * @return
	 */

	private static String convertDocumentToString(Document doc) {
		// Instantiate the Factory
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			// below code to remove XML declaration
			// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			return output;
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return null;
	}

}

