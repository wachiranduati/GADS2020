package nduati.soc.gads2020;

//FIRST THING....check out the commented out code under the class
// so assuming that you've seen the data we're receiving from the api
// we need to retrieve only subsection of it as that's the only part we need in our context
// hence we created this POJO class that represents what a single book will cotain
// remember that the data we retrieved contains multiple books so this is how we're going to retrieve it
class Book {
    public String id;
    public String title; // field names used match with those provided by the api
    public String subtitle;
    public String[] authors;
    public String publishers;
    public String publishedDate;

    // the constructor below will help us assign values to the Books we create remember classes are just blueprints...
    // they only become objects i.e (blue print analogy -> actual houses) once we add data to them
    // we're using a constructor because with this we are able to pass data to the class and have the values assign to the field in the class
    public Book(String id, String title, String subtitle, String[] authors, String publishers, String publishedDate) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.authors = authors;
        this.publishers = publishers;
        this.publishedDate = publishedDate;
    }
}
//NOTE THAT OUR DATA CURRENTLY LOOKS LIKE THIS...THE ONE WE'RE RETRIEVING FROM GOOGLE APIS

//{
//        "kind": "books#volumes",
//        "totalItems": 454,
//        "items": [
//        {
//        "kind": "books#volume",
//        "id": "C4_5MCUd6ucC",
//        "etag": "OJ9cy9Y5q7Q",
//        "selfLink": "https://www.googleapis.com/books/v1/volumes/C4_5MCUd6ucC",
//        "volumeInfo": {
//        "title": "Joy of Cooking",
//        "authors": [
//        "Irma S. Rombauer",
//        "Marion Rombauer Becker"
//        ],
//        "publisher": "Simon and Schuster",
//        "publishedDate": "1975",
//        "description": "Detailed information on foods and cooking techniques accompany fundamental recipes for hors d'oeuvres, soups, salads, main dishes, side dishes, breads, pies, cookies, candies, and desserts",
//        "industryIdentifiers": [
//        {
//        "type": "ISBN_13",
//        "identifier": "9780026045704"
//        },
//        {
//        "type": "ISBN_10",
//        "identifier": "0026045702"
//        }
//        ],
//        "readingModes": {
//        "text": false,
//        "image": false
//        },
//        "pageCount": 915,
//        "printType": "BOOK",
//        "categories": [
//        "Cooking"
//        ],
//        "averageRating": 4.5,
//        "ratingsCount": 16,
//        "maturityRating": "NOT_MATURE",
//        "allowAnonLogging": false,
//        "contentVersion": "0.4.1.0.preview.0",
//        "panelizationSummary": {
//        "containsEpubBubbles": false,
//        "containsImageBubbles": false
//        },
//        "imageLinks": {
//        "smallThumbnail": "http://books.google.com/books/content?id=C4_5MCUd6ucC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api",
//        "thumbnail": "http://books.google.com/books/content?id=C4_5MCUd6ucC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
//        },
//        "language": "en",
//        "previewLink": "http://books.google.com/books?id=C4_5MCUd6ucC&printsec=frontcover&dq=cooking&hl=&cd=1&source=gbs_api",
//        "infoLink": "http://books.google.com/books?id=C4_5MCUd6ucC&dq=cooking&hl=&source=gbs_api",
//        "canonicalVolumeLink": "https://books.google.com/books/about/Joy_of_Cooking.html?hl=&id=C4_5MCUd6ucC"
//        },
//        "saleInfo": {
//        "country": "KE",
//        "saleability": "NOT_FOR_SALE",
//        "isEbook": false
//        },
//        "accessInfo": {
//        "country": "KE",
//        "viewability": "PARTIAL",
//        "embeddable": true,
//        "publicDomain": false,
//        "textToSpeechPermission": "ALLOWED_FOR_ACCESSIBILITY",
//        "epub": {
//        "isAvailable": false
//        },
//        "pdf": {
//        "isAvailable": false
//        },
//        "webReaderLink": "http://play.google.com/books/reader?id=C4_5MCUd6ucC&hl=&printsec=frontcover&source=gbs_api",
//        "accessViewStatus": "SAMPLE",
//        "quoteSharingAllowed": false
//        },
//        "searchInfo": {
//        "textSnippet": "Detailed information on foods and cooking techniques accompany fundamental recipes for hors d&#39;oeuvres, soups, salads, main dishes, side dishes, breads, pies, cookies, candies, and desserts"
//        }
//        },
//        {
//        "kind": "books#volume",
//        "id": "IFiIg7elnwIC",
//        "etag": "7hGaEHum47w",
//        "selfLink": "https://www.googleapis.com/books/v1/volumes/IFiIg7elnwIC",
//        "volumeInfo": {
//        "title": "Cooking in Ancient Civilizations",
//        "authors": [
//        "Cathy K. Kaufman"
//        ],
//        "publisher": "Greenwood Publishing Group",
//        "publishedDate": "2006",
//        "description": "Examines cooking as an integral part of Acient civilizations.",
//        "industryIdentifiers": [
//        {
//        "type": "ISBN_10",
//        "identifier": "0313332045"
//        },
//        {
//        "type": "ISBN_13",
//        "identifier": "9780313332043"
//        }
//        ],
//        "readingModes": {
//        "text": true,
//        "image": true
//        },
//        "pageCount": 224,
//        "printType": "BOOK",
//        "categories": [
//        "Cooking"
//        ],
//        "averageRating": 5,
//        "ratingsCount": 2,
//        "maturityRating": "NOT_MATURE",
//        "allowAnonLogging": true,

