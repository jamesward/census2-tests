using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace silverlight_xml
{
    public class CensusItem
    {
        public int itemId { get; set; }
        public int age { get; set; }
        public string classOfWorker { get; set; }
        public string education { get; set; }
        public string maritalStatus { get; set; }
        public string race { get; set; }
        public string sex { get; set; }
    }
}