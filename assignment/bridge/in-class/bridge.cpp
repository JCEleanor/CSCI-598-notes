#include <iostream>
#include <memory>
#include <string>

// ---------------- Implementation Hierarchy ----------------
class Exporter
{
public:
    virtual ~Exporter() = default;
    virtual void exportReport(const std::string &reportContent) = 0;
};

class PdfExporter : public Exporter
{
public:
    void exportReport(const std::string &reportContent) override
    {
        std::cout << "[PDF Export] " << reportContent << std::endl;
    }
};

class CsvExporter : public Exporter
{
public:
    void exportReport(const std::string &reportContent) override
    {
        std::cout << "[CSV Export] " << reportContent << std::endl;
    }
};

class XmlExporter : public Exporter
{
public:
    void exportReport(const std::string &reportContent) override
    {
        std::cout << "[XML Export] " << reportContent << std::endl;
    }
};

// ---------------- Abstraction Hierarchy ----------------
class Report
{
protected:
    std::shared_ptr<Exporter> exporter; // Bridge via composition
public:
    Report(std::shared_ptr<Exporter> exp) : exporter(exp) {}
    virtual ~Report() = default;

    virtual std::string generateContent() = 0;

    void exportReport()
    {
        exporter->exportReport(generateContent());
    }
};

class FinancialReport : public Report
{
public:
    FinancialReport(std::shared_ptr<Exporter> exp) : Report(exp) {}
    std::string generateContent() override
    {
        return "Financial Report Data";
    }
};

class SalesReport : public Report
{
public:
    SalesReport(std::shared_ptr<Exporter> exp) : Report(exp) {}
    std::string generateContent() override
    {
        return "Sales Report Data";
    }
};

class CustomerReport : public Report
{
public:
    CustomerReport(std::shared_ptr<Exporter> exp) : Report(exp) {}
    std::string generateContent() override
    {
        return "Customer Report Data";
    }
};

// ---------------- Client Simulation ----------------
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
