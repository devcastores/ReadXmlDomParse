package com.grupocastores.mcc.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.StringReader;
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
				+ "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\" Version=\"3.3\" Serie=\"A\" Folio=\"1144\" Fecha=\"2021-09-06T11:46:11\" Sello=\"BgZHHo1D8Xxr0oy7AzMNvn4Ny+TxsJlyY8czObT38QShO6RN2HUGK/SNuqBkwSGU4DdWlJ93eSpv0AxWwMxXClNJMOOt5HTq+J2yj9wUUwnQOnM0iVZ7ZDkUunyu2m+/6m0P0xrzf/3w7uw6GMjQ14a7kXeKdeXlcuEfMvDidQJ1k0xAldgGr6KWOmfw1rM+uWldvKmONfkhog8kpwpWVua867sAuioNW35KO+R5iOjxC+imx5mpePJqGcYZIgHKesw08BSOROARwE+MPiCQ79MNpKhkQSnmVr3p81iUk6cGJObzQAlBkFUlLiZcl7DmOst+kQM/DLF1JxEvpqnSkw==\" FormaPago=\"01\" NoCertificado=\"00001000000503989962\" Certificado=\"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\" SubTotal=\"155\" Moneda=\"MXN\" Total=\"155\" TipoDeComprobante=\"I\" MetodoPago=\"PUE\" LugarExpedicion=\"97295\">\r\n"
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

		
		try {

			/*
			 * >>> TEST
			 * select string xmlStr
			 */
			
			String xmlStr = xmlStr_1;
			
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

}

