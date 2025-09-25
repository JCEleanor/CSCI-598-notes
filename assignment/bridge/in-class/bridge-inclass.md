### Task

1. Define two separate hierarchies: one for report types (Abstraction) and one for export formats (Implementation).

2. Create a bridge between the two hierarchies using composition.

- report types (Abstraction)

```c++
class Report {

protected:
    // bridge between the report types and export type
    std::shared_ptr<Exporter> exporter;

public:
    Report(){}
    void export(){
        exporter->exoportReport('some content')
    }

}

class FinancialReport : public Report {
public:
    FinancialReport(std::shared_ptr<Exporter> exp) : Report(exp) {}
    std::string generateContent() override {
        return "Financial Report Data";
    }
};

class SalesReport : public Report {
public:
    SalesReport(std::shared_ptr<Exporter> exp) : Report(exp) {}
    std::string generateContent() override {
        return "Sales Report Data";
    }
};

class CustomerReport : public Report {
public:
    CustomerReport(std::shared_ptr<Exporter> exp) : Report(exp) {}
    std::string generateContent() override {
        return "Customer Report Data";
    }
};
```

- export formats (Implementation)

```c++
class Exporter {
public:
    virtual void exportReport(const std::string){};
}

class PdfExporter : public Exporter {
public:
    void exportReport(const std::string reportContent) override
    {
        std::cout << "[PDF Export] " << reportContent << std::endl;
    }
}

class CsvExporter : public Exporter {
public:
    void exportReport(const std::string reportContent) override
    {
        std::cout << "[CSV Export] " << reportContent << std::endl;
    }
}

class XmlExporter : public Exporter {
public:
    void exportReport(const std::string reportContent) override
    {
        std::cout << "[XML Export] " << reportContent << std::endl;
    }
}
```

3. Write code/pseudocode to show how a client can generate a FinancialReport to PDF and a SalesReport to CSV without having to know the specifics of the underlying export format.

4. Draw a class diagram to illustrate the relationships between the classes and interfaces.

Hint: Use dependency injection to link the report types to the export formats.

### Client Code: Behavior Simulation

Write code or class stubs to simulate:

A FinancialReport being exported to a PDFExporter.

A SalesReport being exported to a CsvExporter.

### Extensibility in Action:

Imagine a third report type, CustomerReport, and a new export format, XmlExporter.

Extend your design to support both without modifying any existing classes.

```c++
int main()
{
    // FinancialReport → PDF
    auto pdfExporter = std::make_shared<PdfExporter>();
    FinancialReport financial(pdfExporter);
    financial.exportReport();

    // SalesReport → CSV
    auto csvExporter = std::make_shared<CsvExporter>();
    SalesReport sales(csvExporter);
    sales.exportReport();

    // CustomerReport → XML
    auto xmlExporter = std::make_shared<XmlExporter>();
    CustomerReport customer(xmlExporter);
    customer.exportReport();

    return 0;
}
```
