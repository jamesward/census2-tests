using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Xml;
using System.Xml.Linq;

namespace silverlight_xml
{
    public partial class MainPage : UserControl
    {
        IDictionary<string, string> queryStrings;

        private long startRequestTime;
        private long startParseTime;
        private long startRenderTime;

        private long requestTime;
        private long parseTime;
        private long renderTime;

        public MainPage()
        {
            InitializeComponent();

            queryStrings = System.Windows.Browser.HtmlPage.Document.QueryString;

            WebClient client = new WebClient();
            client.DownloadStringCompleted += new DownloadStringCompletedEventHandler(responseHandler);

            String url = "servlet/CensusServiceServlet?command=getXML" +
                "&testId=silverlight_xml" +
                "&rows=" + queryStrings["numRows"] + 
                "&timestamp=" + DateTime.Now.ToUniversalTime().Ticks + 
                "&sendCensusResultURL=" + queryStrings["sendCensusResultURL"] +
                "&clientId=" + queryStrings["clientId"];
            if (queryStrings["enableGZip"] == "true")
            {
                url += "&gzip=true";
            }

            Uri uri = new Uri(url, UriKind.Relative);
            client.DownloadStringAsync(uri);

            startRequestTime = DateTime.Now.ToUniversalTime().Ticks;
        }

        void responseHandler(object sender, DownloadStringCompletedEventArgs e)
        {
            if (e.Error == null)
            {
                requestTime = new TimeSpan(DateTime.Now.ToUniversalTime().Ticks - startRequestTime).Milliseconds;

                startParseTime = DateTime.Now.ToUniversalTime().Ticks;

                XDocument xmlItems = XDocument.Parse(e.Result);

                var source = from item in xmlItems.Descendants("item")
                             select new CensusItem
                             {
                                 itemId = (int)item.Element("itemId"),
                                 age = (int)item.Element("age"),
                                 classOfWorker = ((string)item.Element("classOfWorker")).Trim(),
                                 education = ((string)item.Element("education")).Trim(),
                                 maritalStatus = ((string)item.Element("maritalStatus")).Trim(),
                                 race = ((string)item.Element("race")).Trim(),
                                 sex = ((string)item.Element("sex")).Trim()
                             };

                parseTime = new TimeSpan(DateTime.Now.ToUniversalTime().Ticks - startParseTime).Milliseconds;

                startRenderTime = DateTime.Now.ToUniversalTime().Ticks;

                //System.Diagnostics.Debug.WriteLine("setting data");

                dg.ItemsSource = source.ToList<CensusItem>();

                //System.Diagnostics.Debug.WriteLine("data set");
            }
            else
            {
                System.Diagnostics.Debug.WriteLine(e.ToString());
            }
        }

        private void sendResult(String resultType, String resultData)
        {
            WebClient client = new WebClient();
            client.DownloadStringAsync(new Uri(queryStrings["sendCensusResultURL"] +
                "?clientId=" + queryStrings["clientId"] +
                "&testId=" + "silverlight_xml" +
                "&resultType=" + resultType +
                "&resultData=" + resultData +
                "&gzip=" + queryStrings["enableGZip"] +
                "&numRows=" + queryStrings["numRows"]));
        }

        private void dg_LayoutUpdated(object sender, EventArgs e)
        {
            if (startRenderTime != 0)
            {
                renderTime = new TimeSpan(DateTime.Now.ToUniversalTime().Ticks - startRenderTime).Milliseconds;

                // send the results
                sendResult("requestTime", requestTime.ToString());
                sendResult("parseTime", parseTime.ToString());
                sendResult("renderTime", renderTime.ToString());
                sendResult("memorySize", GC.GetTotalMemory(false).ToString());
            }
        }
    }
}